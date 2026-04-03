#!/usr/bin/env python3
"""
format-checker.py - 检查文档是否符合 quality-standards 的格式要求

根据 docs/quality-standards.md 中定义的标准检查每篇文档是否包含必需章节。

用法：
    python tools/format-checker.py [目录路径]

示例：
    python tools/format-checker.py .
    python tools/format-checker.py ./01-agent-basics
"""

import os
import re
import sys
from pathlib import Path
from dataclasses import dataclass, field


# 必需章节的检测规则（正则匹配章节标题）
REQUIRED_SECTIONS = [
    {
        'name': '概念与原理',
        'pattern': re.compile(r'^##\s+[一二三四五六七八九十\d]+[、.．]\s*概念.{0,10}原理', re.MULTILINE),
        'description': '必须包含"概念与原理"章节',
    },
    {
        'name': '技术详解',
        'pattern': re.compile(r'^##\s+[一二三四五六七八九十\d]+[、.．]\s*技术详解', re.MULTILINE),
        'description': '必须包含"技术详解"章节',
    },
    {
        'name': 'Java 代码示例',
        'pattern': re.compile(r'^##\s+[一二三四五六七八九十\d]+[、.．]\s*Java\s*代码示例', re.MULTILINE),
        'description': '必须包含"Java 代码示例"章节',
    },
    {
        'name': '最佳实践',
        'pattern': re.compile(r'^##\s+[一二三四五六七八九十\d]+[、.．]\s*最佳实践', re.MULTILINE),
        'description': '必须包含"最佳实践"章节',
    },
    {
        'name': '常见问题',
        'pattern': re.compile(r'^##\s+[一二三四五六七八九十\d]+[、.．]\s*常见问题', re.MULTILINE),
        'description': '必须包含"常见问题"章节',
    },
]

# 推荐元素的检测规则
RECOMMENDED_ELEMENTS = [
    {
        'name': 'Java 代码块',
        'pattern': re.compile(r'```java', re.MULTILINE),
        'description': '建议包含 Java 代码块',
    },
    {
        'name': 'Mermaid 图表',
        'pattern': re.compile(r'```mermaid', re.MULTILINE),
        'description': '建议包含 mermaid 图表',
    },
    {
        'name': 'Maven 依赖',
        'pattern': re.compile(r'<dependency>|<groupId>', re.MULTILINE),
        'description': '建议包含 Maven 依赖配置',
    },
    {
        'name': '对比表格',
        'pattern': re.compile(r'^\|.+\|.+\|', re.MULTILINE),
        'description': '建议包含对比表格',
    },
]

# 应该跳过格式检查的文件（如 README、索引文件等）
SKIP_FILES = {
    'README.md',
    'AGENTS.md',
    'TODO-modules.md',
}

# 应该跳过的目录
SKIP_DIRS = {
    '.git',
    'node_modules',
    '__pycache__',
    'docs',      # docs/ 下的管理文档不需要遵循内容格式
    'tools',     # tools/ 目录
    'modules',   # archive 目录
    'module-1',  # Java 代码模块
}

# 需要进行完整格式检查的文档目录（主内容目录）
CONTENT_DIRS = {
    '01-agent-basics',
    '02-llm-fundamentals',
    '03-llm-models-research',
    '04-agent-frameworks',
    '05-llm-apis-providers',
    '06-rag-knowledge-retrieval',
    '07-multi-agent-systems',
    '08-model-safety-alignment',
    '09-performance-monitoring',
    '10-practical-cases',
}


@dataclass
class CheckResult:
    """单个文件的检查结果"""
    file: str
    missing_sections: list[str] = field(default_factory=list)
    missing_recommended: list[str] = field(default_factory=list)
    warnings: list[str] = field(default_factory=list)

    @property
    def passed(self) -> bool:
        return len(self.missing_sections) == 0

    @property
    def score(self) -> int:
        """质量评分（0-100）"""
        total = len(REQUIRED_SECTIONS) + len(RECOMMENDED_ELEMENTS)
        missing = len(self.missing_sections) + len(self.missing_recommended)
        return max(0, int((total - missing) / total * 100))


def should_check_file(md_file: Path, root: Path) -> bool:
    """判断文件是否需要进行格式检查"""
    # 跳过特定文件名
    if md_file.name in SKIP_FILES:
        return False

    # 检查是否在内容目录中
    try:
        rel = md_file.relative_to(root)
        parts = rel.parts
        if len(parts) >= 2 and parts[0] in CONTENT_DIRS:
            return True
    except ValueError:
        pass

    return False


def check_file(md_file: Path) -> CheckResult:
    """
    检查单个 markdown 文件的格式
    
    :param md_file: 要检查的 markdown 文件
    :return: 检查结果
    """
    result = CheckResult(file=str(md_file))

    try:
        content = md_file.read_text(encoding='utf-8')
    except Exception as e:
        result.warnings.append(f"无法读取文件: {e}")
        return result

    # 检查必需章节
    for section in REQUIRED_SECTIONS:
        if not section['pattern'].search(content):
            result.missing_sections.append(section['name'])

    # 检查推荐元素
    for element in RECOMMENDED_ELEMENTS:
        if not element['pattern'].search(content):
            result.missing_recommended.append(element['name'])

    # 额外检查：文档长度
    line_count = content.count('\n')
    if line_count < 50:
        result.warnings.append(f"文档较短（{line_count} 行），建议补充更多内容")

    # 检查一级标题
    if not re.match(r'^#\s+\S', content):
        result.warnings.append("文档应以一级标题（# 标题）开头")

    return result


def check_directory(root: Path) -> tuple[int, int, list[CheckResult]]:
    """
    递归检查目录中所有符合条件的 markdown 文件
    
    :param root: 根目录
    :return: (检查文件数, 通过文件数, 所有检查结果)
    """
    all_results = []
    checked = 0
    passed = 0

    for md_file in sorted(root.rglob('*.md')):
        # 检查是否应跳过
        if any(skip in md_file.parts for skip in SKIP_DIRS):
            continue

        if not should_check_file(md_file, root):
            continue

        checked += 1
        result = check_file(md_file)
        result.file = str(md_file.relative_to(root))

        if result.passed:
            passed += 1

        all_results.append(result)

    return checked, passed, all_results


def print_report(root: Path, checked: int, passed: int, results: list[CheckResult]):
    """输出格式检查报告"""
    print("=" * 60)
    print("📋 文档格式检查报告")
    print("=" * 60)
    print(f"根目录：{root}")
    print(f"检查文件数：{checked}")
    print(f"通过文件数：{passed}")
    print(f"不合格文件数：{checked - passed}")

    if checked > 0:
        pass_rate = passed / checked * 100
        avg_score = sum(r.score for r in results) / len(results) if results else 0
        print(f"通过率：{pass_rate:.1f}%")
        print(f"平均质量评分：{avg_score:.0f}/100")

    print()

    # 显示不合格文件
    failed = [r for r in results if not r.passed]
    if failed:
        print(f"❌ 不合格文件（{len(failed)} 个）：")
        print()
        for result in sorted(failed, key=lambda r: r.score):
            print(f"  📄 {result.file} (评分: {result.score}/100)")
            if result.missing_sections:
                print(f"     ❌ 缺少必需章节: {', '.join(result.missing_sections)}")
            if result.missing_recommended:
                print(f"     ⚠️  缺少推荐元素: {', '.join(result.missing_recommended)}")
            if result.warnings:
                for w in result.warnings:
                    print(f"     ℹ️  {w}")
            print()
    else:
        print("✅ 所有文档均符合格式要求！")
        print()

    # 显示完全通过但有推荐元素缺失的文件
    warn_only = [r for r in results if r.passed and (r.missing_recommended or r.warnings)]
    if warn_only:
        print(f"⚠️  格式通过但有改进建议的文件（{len(warn_only)} 个）：")
        print()
        for result in warn_only[:5]:  # 只显示前 5 个
            print(f"  📄 {result.file}")
            if result.missing_recommended:
                print(f"     建议添加: {', '.join(result.missing_recommended)}")
        if len(warn_only) > 5:
            print(f"  ... 还有 {len(warn_only) - 5} 个文件（使用 -v 查看全部）")
        print()

    print("=" * 60)
    print()
    print("必需章节说明：")
    for s in REQUIRED_SECTIONS:
        print(f"  ✓ {s['name']}: {s['description']}")
    print()
    print("参考标准：docs/quality-standards.md")

    return len(failed) == 0


def main():
    # 确定检查目录
    verbose = '-v' in sys.argv or '--verbose' in sys.argv
    args = [a for a in sys.argv[1:] if not a.startswith('-')]

    if args:
        root = Path(args[0]).resolve()
    else:
        root = Path(__file__).resolve().parent.parent

    if not root.exists():
        print(f"[ERROR] 目录不存在：{root}")
        sys.exit(1)

    if not root.is_dir():
        print(f"[ERROR] 路径不是目录：{root}")
        sys.exit(1)

    print(f"正在检查目录：{root}\n")
    checked, passed, results = check_directory(root)
    success = print_report(root, checked, passed, results)

    sys.exit(0 if success else 1)


if __name__ == '__main__':
    main()
