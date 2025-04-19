# EBU6304_Group58 – Software Engineering Group Project
AI Empowered Personal Finance Tracker(Software Development Using Agile Methods)


功能 #14：查看剩余会员时间
- 类：User, UserLoader, UserPanel
- 数据：data/users.csv
- 显示会员到期日期和剩余天数，若过期则高亮提示
- 已通过 JUnit 测试验证逻辑
- 开发者：@Keeper0824 (Haoran Sun)


## 功能 #15: 用户管理 - 添加新用户

- 类：CashFlowController, CashFlowView, Transaction
- 数据：data/users.csv
- 描述：
  - Transaction 类：用于存储交易类型（收入或支出）和金额。
  -  CashFlowController 类：加载交易数据，并更新显示和图表：
  - 从 CSV 文件中加载交易数据。
  - 每隔 2 秒逐步显示交易数据（模拟动画效果）。
  - 更新收入、支出和净收入（net）的显示。
  - 使用 PieChart 和 BarChart 显示收入与支出的比例，以及每月现金流。


- 测试：已通过 JUnit 测试确保新增用户功能正确，能成功将 `InOutcome.csv` 文件中的收支信息顺利绘图分析。（CSV中有收支类型及数目）
- 开发者：@Keeper0824 (HaoRan Sun)
