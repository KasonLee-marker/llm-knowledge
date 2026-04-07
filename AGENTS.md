# AGENTS.md - LLM Knowledge Base 导航

> 本文档是智能体的工作导航地图，人类通过编辑 coverage-matrix 和 exec-plans 来指导内容生成方向。

---

## 项目目标

维护一份**全面、结构化、持续更新**的 LLM 与 AI Agent 知识库，面向 Java 后端开发者，涵盖从理论到工程落地的完整链路。

---

## 目录结构

```
llm-knowledge/
├── AGENTS.md              # 本文件 - 项目导航地图
├── README.md              # 项目主入口 - 面向用户的介绍
├── TODO-modules.md        # 模块任务列表
│
├── docs/                  # 知识库文档（Harness 基础设施）
│   ├── architecture.md    # 知识体系架构
│   ├── coverage-matrix.md # 知识点覆盖矩阵（核心）
│   ├── quality-standards.md # 内容质量标准
│   └── exec-plans/        # 执行计划（待完成任务）
│       └── README.md
│
├── tools/                 # 工具链
│   ├── coverage-checker.py # 覆盖率扫描工具
│   ├── format-checker.py  # 文档格式校验工具
│   └── link-checker.py    # 链接有效性检查工具
│
├── 01-agent-basics/       # ✅ Agent 基础
├── 02-llm-fundamentals/   # ✅ LLM 基础
├── 03-llm-models-research/ # ✅ 模型研究
├── 04-agent-frameworks/   # ✅ Agent 框架
├── 05-llm-apis-providers/ # ✅ APIs 与供应商
├── 06-rag-knowledge-retrieval/ # ✅ RAG 检索
├── 07-multi-agent-systems/ # ✅ 多智能体系统
├── 08-model-safety-alignment/ # ✅ 安全与对齐
├── 09-performance-monitoring/ # ✅ 性能优化
└── 10-practical-cases/    # ✅ 实战案例
```

**图例：**
- ✅ 已完成 - 内容较完整
- ⚠️ 待完善 - 有骨架需补充
- ❌ 待创建 - 目录待建立

---

## 内容质量标准

详见 [docs/quality-standards.md](./docs/quality-standards.md)

**核心要求：**
1. **面向 Java 开发者** - 所有示例使用 Java，框架优先介绍 Java 生态
2. **工程导向** - 不仅讲原理，还要讲如何在 Java 项目中落地
3. **代码完整** - 提供可运行的 Java 代码示例
4. **对比分析** - 多个方案对比，知道何时用哪个
5. **实战优先** - 结合真实工程场景，避免纯理论

---

## 当前缺口

详见 [docs/coverage-matrix.md](./docs/coverage-matrix.md)

**总体完成度：约 100%**

主要模块已全部完成，剩余工作：
- 持续更新 03-llm-models-research 的最新模型信息
- 完善部分模块的 Java 实战案例

---

## 贡献工作流（Harness 模式）

### 对于人类（你）

1. **查看缺口** - 运行 `python tools/coverage-checker.py` 或查看 coverage-matrix.md
2. **创建执行计划** - 在 `docs/exec-plans/` 创建任务描述
3. **触发生成** - 通知本汪（开发小柴）启动内容生成
4. **审核合并** - 审核生成的 PR，确认后合并

### 对于智能体（Claude）

1. **读取 AGENTS.md** - 理解项目结构和标准
2. **查阅 coverage-matrix** - 确定当前缺口
3. **生成内容** - 按 quality-standards 创建文档
4. **自检** - 运行 coverage-checker 验证
5. **提交** - 使用 `gh api` 推送文件到 GitHub（git push 网络不稳定）
6. **创建 PR** - 使用 `gh pr create` 创建 Pull Request

---

## 快速开始

```bash
# 1. 查看当前覆盖情况
python tools/coverage-checker.py

# 2. 查看待完成任务
ls docs/exec-plans/

# 3. 开始新任务（人类操作）
# 编辑 docs/exec-plans/xxx.md，然后通知本汪
```

---

## 更新日志

- **2026-04-03** - 补齐全部缺口：新增 MCP、Spring AI、LLM 评估文档；新增 2 个实战案例；更新 03 模块至 2026 最新模型
- **2026-03-31** - 初始化 Harness Engineering 基础设施（AGENTS.md, coverage-matrix, docs/）
- **2026-03-10** - 完成 06-10 模块（RAG、多智能体、安全、性能、实战案例）
- **2026-03-09** - 完善 04-05 模块（框架、APIs）
- **2026-03-09** - 初始版本，包含 01-03 模块基础

---

> 💡 **提示**：本文档是智能体的工作导航，人类通过编辑 coverage-matrix 和 exec-plans 来指导内容生成方向。
