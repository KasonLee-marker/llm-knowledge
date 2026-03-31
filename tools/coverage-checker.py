#!/usr/bin/env python3
"""
LLM Knowledge Base 覆盖率检查工具
扫描项目目录，生成覆盖矩阵报告
"""

import os
import json
from pathlib import Path
from datetime import datetime

# 模块配置
MODULES = {
    "01-agent-basics": {
        "name": "Agent 基础",
        "expected": 6,
        "priority": "P0"
    },
    "02-llm-fundamentals": {
        "name": "LLM 基础",
        "expected": 6,
        "priority": "P0"
    },
    "03-llm-models-research": {
        "name": "模型研究",
        "expected": 12,
        "priority": "P1"
    },
    "04-agent-frameworks": {
        "name": "Agent 框架",
        "expected": 6,
        "priority": "P0"
    },
    "05-llm-apis-providers": {
        "name": "APIs 与供应商",
        "expected": 6,
        "priority": "P0"
    },
    "06-rag-knowledge-retrieval": {
        "name": "RAG 检索",
        "expected": 8,
        "priority": "P0"
    },
    "07-multi-agent-systems": {
        "name": "多智能体系统",
        "expected": 7,
        "priority": "P0"
    },
    "08-model-safety-alignment": {
        "name": "安全与对齐",
        "expected": 7,
        "priority": "P0"
    },
    "09-performance-monitoring": {
        "name": "性能优化",
        "expected": 6,
        "priority": "P0"
    },
    "10-practical-cases": {
        "name": "实战案例",
        "expected": 5,
        "priority": "P0"
    }
}

def count_md_files(directory):
    """统计目录下的 markdown 文件数量（不包括 README）"""
    if not os.path.exists(directory):
        return 0
    
    count = 0
    for f in os.listdir(directory):
        if f.endswith('.md') and not f.startswith('README'):
            count += 1
    return count

def check_module(module_dir, config):
    """检查单个模块的状态"""
    actual = count_md_files(module_dir)
    expected = config["expected"]
    
    if actual >= expected:
        status = "✅"
        completion = 100
    elif actual > 0:
        status = "⚠️"
        completion = int((actual / expected) * 100)
    else:
        status = "❌"
        completion = 0
    
    return {
        "name": config["name"],
        "status": status,
        "completion": completion,
        "actual": actual,
        "expected": expected,
        "priority": config["priority"]
    }

def main():
    """主函数"""
    print("=" * 60)
    print("LLM Knowledge Base - 覆盖率检查")
    print("=" * 60)
    print()
    
    base_dir = Path(__file__).parent.parent
    results = []
    total_actual = 0
    total_expected = 0
    
    print("模块覆盖情况：")
    print("-" * 60)
    print(f"{'模块':<25} {'状态':<6} {'完成度':<10} {'文档数':<12} {'优先级':<8}")
    print("-" * 60)
    
    for module_dir, config in MODULES.items():
        module_path = base_dir / module_dir
        result = check_module(module_path, config)
        results.append((module_dir, result))
        
        total_actual += result["actual"]
        total_expected += result["expected"]
        
        print(f"{result['name']:<20} {result['status']:<6} "
              f"{result['completion']:>3}%{'':<6} "
              f"{result['actual']}/{result['expected']:<6} "
              f"{result['priority']:<8}")
    
    print("-" * 60)
    
    # 总体统计
    overall_completion = int((total_actual / total_expected) * 100) if total_expected > 0 else 0
    print(f"\n总体完成度: {overall_completion}% ({total_actual}/{total_expected} 文档)")
    
    # 高优先级缺口
    print("\n高优先级缺口 (P0) ：")
    gaps = [(m, r) for m, r in results if r["priority"] == "P0" and r["completion"] < 100]
    if gaps:
        for module, result in gaps:
            print(f"  - {result['name']}: {result['actual']}/{result['expected']} "
                  f"({100 - result['completion']}% 缺口)")
    else:
        print("  ✅ 所有 P0 模块已完成")
    
    # 建议下一步
    print("\n建议下一步：")
    incomplete = [(m, r) for m, r in results if r["completion"] < 100]
    if incomplete:
        incomplete.sort(key=lambda x: (x[1]["priority"] != "P0", -x[1]["completion"]))
        module, result = incomplete[0]
        print(f"  1. 完善 {result['name']} (当前 {result['completion']}%)")
    else:
        print("  ✅ 所有模块已完成，建议更新 03-llm-models-research 最新模型信息")
    
    print()
    print("=" * 60)
    print(f"检查时间: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
    print("=" * 60)

if __name__ == "__main__":
    main()
