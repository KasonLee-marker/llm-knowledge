#!/usr/bin/env python3
"""
link-checker.py - 检查文档内部链接（markdown 文件间的链接）是否有效

用法：
    python tools/link-checker.py [目录路径]

示例：
    python tools/link-checker.py .
    python tools/link-checker.py ./01-agent-basics
"""

import os
import re
import sys
import urllib.parse
from pathlib import Path

# Markdown 链接正则（排除外部 URL 和锚点链接）
LINK_PATTERN = re.compile(r'\[([^\]]*)\]\(([^)]+)\)')
EXTERNAL_PREFIXES = ('http://', 'https://', 'ftp://', 'mailto:')


def is_external_link(link: str) -> bool:
    """判断是否为外部链接"""
    return any(link.startswith(prefix) for prefix in EXTERNAL_PREFIXES)


def is_anchor_only(link: str) -> bool:
    """判断是否为纯锚点链接（如 #section）"""
    return link.startswith('#')


def resolve_link(source_file: Path, link: str, root: Path) -> Path:
    """
    将相对链接解析为绝对路径
    
    :param source_file: 包含该链接的源文件路径
    :param link: 链接目标（可包含锚点）
    :param root: 文档根目录
    :return: 解析后的目标文件绝对路径
    """
    # 去掉锚点部分
    link_path = link.split('#')[0]
    if not link_path:
        return None

    # URL 解码
    link_path = urllib.parse.unquote(link_path)

    # 解析相对路径
    source_dir = source_file.parent
    target = (source_dir / link_path).resolve()
    return target


def check_file(md_file: Path, root: Path) -> list[dict]:
    """
    检查单个 markdown 文件中的所有链接
    
    :param md_file: 要检查的 markdown 文件
    :param root: 文档根目录
    :return: 失效链接列表
    """
    broken = []
    try:
        content = md_file.read_text(encoding='utf-8')
    except Exception as e:
        print(f"[ERROR] 无法读取文件 {md_file}: {e}")
        return broken

    for line_num, line in enumerate(content.splitlines(), start=1):
        for match in LINK_PATTERN.finditer(line):
            link_text = match.group(1)
            link_target = match.group(2).strip()

            # 跳过外部链接和纯锚点
            if is_external_link(link_target) or is_anchor_only(link_target):
                continue

            target_path = resolve_link(md_file, link_target, root)
            if target_path is None:
                continue

            if not target_path.exists():
                broken.append({
                    'source': str(md_file.relative_to(root)),
                    'line': line_num,
                    'text': link_text,
                    'target': link_target,
                    'resolved': str(target_path.relative_to(root)) if target_path.is_relative_to(root) else str(target_path),
                })

    return broken


def check_directory(root: Path) -> tuple[int, int, list[dict]]:
    """
    递归检查目录中所有 markdown 文件的链接
    
    :param root: 根目录
    :return: (文件总数, 检查链接总数, 失效链接列表)
    """
    all_broken = []
    total_files = 0
    total_links = 0

    # 排除的目录
    exclude_dirs = {'.git', 'node_modules', '__pycache__', '.idea'}

    for md_file in sorted(root.rglob('*.md')):
        # 跳过排除目录
        if any(part in exclude_dirs for part in md_file.parts):
            continue

        total_files += 1
        try:
            content = md_file.read_text(encoding='utf-8')
            # 统计链接数
            links = LINK_PATTERN.findall(content)
            internal_links = [
                l for _, l in links
                if not is_external_link(l.strip()) and not is_anchor_only(l.strip())
            ]
            total_links += len(internal_links)
        except Exception:
            pass

        broken = check_file(md_file, root)
        all_broken.extend(broken)

    return total_files, total_links, all_broken


def print_report(root: Path, total_files: int, total_links: int, broken: list[dict]):
    """输出检查报告"""
    print("=" * 60)
    print("📎 文档内部链接检查报告")
    print("=" * 60)
    print(f"根目录：{root}")
    print(f"扫描文件数：{total_files}")
    print(f"内部链接总数：{total_links}")
    print(f"失效链接数：{len(broken)}")
    print()

    if not broken:
        print("✅ 所有内部链接均有效！")
    else:
        print(f"❌ 发现 {len(broken)} 个失效链接：")
        print()

        # 按源文件分组
        by_file: dict[str, list] = {}
        for item in broken:
            by_file.setdefault(item['source'], []).append(item)

        for source_file, items in sorted(by_file.items()):
            print(f"  📄 {source_file}")
            for item in items:
                print(f"     第 {item['line']} 行: [{item['text']}]({item['target']})")
                print(f"     ↳ 解析路径: {item['resolved']} (文件不存在)")
            print()

    print("=" * 60)
    return len(broken) == 0


def main():
    # 确定检查目录
    if len(sys.argv) > 1:
        root = Path(sys.argv[1]).resolve()
    else:
        # 默认使用脚本所在目录的上级（项目根目录）
        root = Path(__file__).resolve().parent.parent

    if not root.exists():
        print(f"[ERROR] 目录不存在：{root}")
        sys.exit(1)

    if not root.is_dir():
        print(f"[ERROR] 路径不是目录：{root}")
        sys.exit(1)

    print(f"正在检查目录：{root}\n")
    total_files, total_links, broken = check_directory(root)
    success = print_report(root, total_files, total_links, broken)

    sys.exit(0 if success else 1)


if __name__ == '__main__':
    main()
