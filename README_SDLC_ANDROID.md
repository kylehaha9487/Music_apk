# AURA MUSIC v32.0 // Android 原生旗艦版 (Kotlin + Jetpack Compose + Media3)

本專案已依據 **SDLC (Software Development Life Cycle)** 完整重構自 `aura_music.py`，徹底解決 Python 打包 APK 無法啟動、WebView 白屏與鎖定畫面斷音問題。

## 一、架構升級重點
1. **移除不穩定的本地 Python Web Server 與 YouTube IFrame**：改用 Google 官方 **Jetpack Media3 (`ExoPlayer` + `MediaSessionService`)**。
2. **升級多源完整全曲音源引擎 (`AuraRepository.kt`)**：
   - 自動解析 **網易雲完整全曲 MP3 (>1.1MB 驗證)**
   - **Piped / Invidious 開放式高音質 M4A 純音訊流**
   - **Audius / Jamendo 開放人聲完整全曲** 與 **24H 全球音樂電台**
   - **手機本機無損音樂庫 (`MediaStore.Audio`)**
3. **保留雙語同步歌詞與 Google GTX 繁中翻譯**：支援毫秒級對齊、`±0.3s` 微調與「🎯 對齊下一句」。

## 二、如何產出手機可安裝的 `.apk` 檔案？

### 方法 A：使用 Android Studio 一鍵安裝（推薦）
1. 解壓縮本專案資料夾。
2. 打開 **Android Studio** -> 點選 **Open** -> 選擇解壓縮後的資料夾。
3. 等待 Gradle 自動同步完成後，接上 Android 手機（或啟動模擬器），點擊上方綠色 **Run (`▶`)** 按鈕即可直接安裝到手機，或點選選單 **Build > Build Bundle(s) / APK(s) > Build APK(s)** 產出獨立安裝包。

### 方法 B：免裝軟體！使用 GitHub Actions 雲端自動打包 APK
1. 本專案已內建 `.github/workflows/build_android_apk.yml`。
2. 將解壓縮後的檔案上傳至你的 GitHub Repository。
3. 點擊 GitHub 頁面上方的 **Actions** 頁籤，約 3 分鐘即可直接下載編譯好的 `AuraMusic-v32-Android-Native-APK.zip` 安裝至手機！
