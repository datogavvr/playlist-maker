# 🎵 Playlist Maker

**Playlist Maker** — Android-приложение для поиска треков и создания музыкальных плейлистов. Проект учитывает современный подход к разработке мобильных приложений: чистая архитектура, разделение на модули, Jetpack Compose и грамотная работа с данными (сеть + локальная база).

## 📱 Возможности

- 📂 Создание и управление плейлистами
- 🔎 Поиск треков через сетевой API
- 💾 Сохранение информации в локальной базе данных
- 📜 Просмотр истории поисковых запросов
- 🌗 Поддержка светлой и темной темы
- 🌍 Локализация на русский и английский языки
- ⚙️ Удобная и стабильная сборка через Gradle

---

## 🖼️ Скриншоты

| <div align="center">**Главная**</div> | <div align="center">**Поиск**</div> | <div align="center">**Экран трека**</div> |
|:--:|:--:|:--:|
| <img src="screenshots/main_light.png" width="48%"/> <img src="screenshots/main_dark.png" width="48%"/> | <img src="screenshots/search_light.png" width="48%"/> <img src="screenshots/search_dark.png" width="48%"/> | <img src="screenshots/track_light.png" width="48%"/> <img src="screenshots/track_dark.png" width="48%"/> |
| <div align="center">**Экран трека**</div> | <div align="center">**Добавление трека**</div> | <div align="center">**Список плейлистов**</div> |
| <img src="screenshots/track_light.png" width="48%"/> <img src="screenshots/track_dark.png" width="48%"/> | <img src="screenshots/add_track_light.png" width="48%"/> <img src="screenshots/add_track_dark.png" width="48%"/> | <img src="screenshots/playlists_light.png" width="48%"/> <img src="screenshots/playlists_dark.png" width="48%"/> |
| <div align="center">**Создание плейлиста**</div> | <div align="center">**Экран плейлиста**</div> | <div align="center">**Параметры плейлиста**</div> |
| <img src="screenshots/add_playlist_light.png" width="48%"/> <img src="screenshots/add_playlist_dark.png" width="48%"/> | <img src="screenshots/my_playlist_light.png" width="48%"/> <img src="screenshots/my_playlist_dark.png" width="48%"/> | <img src="screenshots/playlist_info_light.png" width="48%"/> <img src="screenshots/playlist_info_dark.png" width="48%"/> |
| <div align="center">**Избранное**</div> | <div align="center">**Настройки**</div> |  |
| <img src="screenshots/favorite_light.png" width="48%"/> <img src="screenshots/favorite_dark.png" width="48%"/> | <img src="screenshots/settings_light.png" width="48%"/> <img src="screenshots/settings_dark.png" width="48%"/> |

---

## 🛠️ Технологии и архитектура

| Технология / Паттерн | Назначение |
|------------------------|------------|
| **Kotlin** | Язык разработки приложения |
| **Clean Architecture + MVVM** | Организация кода и разделение ответственности |
| **Jetpack Compose** | Построение современного интерфейса |
| **Navigation Component (Single Activity)** | Управление переходами между экранами в рамках одной MainActivity |
| **Room Database** | Локальное хранение плейлистов, треков, истории и избранного |
| **Gradle (Kotlin DSL)** | Автоматизация сборки и управления зависимостями |

---

## 🚀 Как запустить (APK файл)

Скачать готовый установочный файл можно здесь:

👉 **[Скачать APK](https://github.com/datogavvr/playlist-maker/releases/latest)**

