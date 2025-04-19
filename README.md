# EBU6304_Group58 – Software Engineering Group Project

AI Empowered Personal Finance Tracker (Software Development Using Agile Methods)

---

## 📦 已实现功能一览

### 🔹 功能 #14：查看高级会员时间

- **类**：User, UserLoader, UserPanel
- **数据**：data/users.csv
- **说明**：显示会员到期日期和剩余天数，若过期则高亮提示
- ✅ 已通过 JUnit 测试验证逻辑
- 👤 开发者：@Keeper0824 (Haoran Sun)

---

### 🔹 功能 #15：用户管理 - 添加新用户

- **类**：CashFlowController, CashFlowView, Transaction
- **数据**：data/users.csv
- **说明**：
  - `Transaction` 类：用于存储交易类型（收入/支出）和金额。
  - `CashFlowController` 类：加载交易数据并更新显示。
  - 从 CSV 文件中加载交易数据。
  - 多种类型交易显示在表格视图中（根据功能需求）。
  - 支持新增收入、支出和净收入（net）的图表。
  - 使用 PieChart 和 BarChart 显示收入与支出的比例，以及每日交易数量。
- ✅ 已通过 JUnit 测试验证用户功能正常，能读取收入类型为 `income.csv` 文件中的收支信息（含类型及数目）
- 👤 开发者：@Keeper0824 (HaoRan Sun)
