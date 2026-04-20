# MiBand Bridge

小米手环数据桥接应用 - 将手环数据接入你自己的应用

## 功能

- 自动采集小米手环数据（心率、步数、睡眠）
- 支持 ROOT 和非 ROOT 两种模式
- 后台服务持续同步数据
- 数据通过 Flow 实时推送到 UI

## 项目结构

```
MiBandBridge/
├── app/src/main/java/com/moonl/mibandbridge/
│   ├── data/
│   │   ├── BandData.kt          # 数据模型
│   │   ├── DataCollector.kt     # 数据采集器接口
│   │   ├── RootDataCollector.kt # ROOT模式 - 直接读数据库
│   │   └── AccessibilityDataCollector.kt  # 非ROOT模式 - 截图OCR
│   ├── service/
│   │   └── DataCollectionService.kt  # 后台数据采集服务
│   └── MainActivity.kt
└── app/src/main/res/layout/
    └── activity_main.xml
```

## 使用方法

### 准备工作

1. 确保手机上安装了「小米运动健康」App
2. 手环已与小米 App 配对并同步过数据

### 安装 APK

APK 文件位置：`app/build/outputs/apk/debug/app-debug.apk`

```bash
# 复制到手机
adb install app/build/outputs/apk/debug/app-debug.apk
```

### 模式选择

#### 方式一：ROOT 模式（推荐，更稳定）

1. 手机已 ROOT
2. 应用会自动检测并使用数据库直读
3. 直接点击「启动数据收集」即可

#### 方式二：非 ROOT 模式

1. 首次打开应用会提示「无ROOT - 使用辅助功能」
2. 需要手动开启辅助功能：
   - 手机设置 → 无障碍 → 已安装的服务 → MiBand Bridge → 开启
3. 回到应用点击「启动数据收集」

### 查看数据

启动后会自动每 30 秒刷新一次数据：
- 步数
- 心率
- 睡眠时长

## 技术方案

### ROOT 模式原理
```
手环 → 小米App → 数据库(/data/data/com.xiaomi.hm.health/databases/) → 你的App
```

### 非 ROOT 模式原理
```
手环 → 小米App(屏幕上显示数据) → 截图 → OCR识别 → 你的App
```

## 待完善

- [ ] 数据库表结构解析（目前是 TODO）
- [ ] OCR 数据采集实现
- [ ] 通知栏显示实时数据
- [ ] 数据导出功能

## 环境要求

- Android SDK 34
- Gradle 8.5
- Kotlin 1.9.22
- minSdk 26

## 构建

```bash
./gradlew assembleDebug
```