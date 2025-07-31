# Lenient Registry Sync

## 描述

这个Mod用来忽略某些“注册表不一致”的提示，使得客户端在无需安装对应Mod的情况下即可加入服务器。

## 兼容性

它现在编译目标是 `Java 17` 的 `Minecraft 1.20.x-Fabric` 兼容性仅在 `1.20.1` 版本上测试通过。它理论上应该支持1.20全版本。
警告：如果使用这个Mod来绕过服务端注册的新的实体/方块/群系可能会发生不可预料的问题。

## 配置

在 `config/` 文件夹中将会生成 `lenient-registry-sync.json` 配置文档。
`enabled` 字段用于启用/关闭这个mod的作用，`ignored_mods` 列表用于指定忽略的项。

## 许可证

此项目采用 `Apache License 2.0` 授权。
