# 💬 PixFlow – Image & Video Viewer App

## 📄 Description
**PixFlow** is a sleek and user-friendly Android app that brings high-quality photos and videos from the **Pexels API** to your fingertips. Browse trending media, search images & videos, mark favorites, share with friends, and download them for offline viewing—all in one seamless experience. Built using **Jetpack Compose**, the app provides a smooth, modern UI and intuitive navigation.

---

## 📚 Table of Contents
- [Features](#features)
- [Installation](#installation)
- [Tech Stack](#tech-stack)
- [Challenges & Solutions](#challenges--solutions)
- [Future Improvements](#future-improvements)
- [Libraries Used](#libraries-used)
- [Contact](#contact)
- [Screenshots](#screenshots)

---

## ✅ Features
- 📸 **Browse Popular Media:** Explore trending images and videos from Pexels.  
- 🔍 **Search Functionality:** Find photos and videos using keywords.  
- ⭐ **Favorites:** Mark images and videos as favorites to access later.  
- 📤 **Share:** Share media instantly with friends and social apps.  
- 💾 **Download for Offline:** Save images and videos to your device for offline viewing.  
- 🎨 **Beautiful UI:** Modern, responsive interface with Jetpack Compose.  
- 🌙 **Dark Mode Support:** Works seamlessly in light and dark themes.  

---

## ⚙️ Installation
Clone the repository:
```bash
git clone https://github.com/your-username/PixFlow.git
cd PixFlow
```

## 🛠 Tech Stack
- Kotlin
- Jetpack Compose
- MVVM Architecture
- Material Design 3
- Room Database – for storing favorites and downloaded media
- Coil – for image loading
- WorkManager – for background tasks like downloads

---

## ⚔️ Challenges & Solutions

| Challenge | Solution |
| --- | --- |
| Handling offline downloads | Stored media locally with `localPath` and `isDownloaded` flags in Room |
| Smooth image & video loading | Used Coil for images and ExoPlayer for videos with caching |
| Efficient UI updates | StateFlow + Compose LazyColumn for performant scrolling |

---

## 🌱 Future Improvements
- 🔍 Add advanced search filters (category, resolution, video length)  
- 🎙️ Add text-to-speech descriptions for images/videos  
- 🌐 Integrate Pexels API updates in real-time  
- 🌟 Daily trending media notifications  
- 🖼️ Add customizable gallery layouts  

---

## 📦 Libraries Used
- Jetpack Compose
- Room Database
- Material Design 3
- Coil
- ExoPlayer
- ViewModel & StateFlow

---

## 📬 Contact
- **Name:** Shriyansh Kushwaha
- **LinkedIn:** https://www.linkedin.com/in/shriyansh-kushwaha-88357a28a 
- **GitHub:** https://github.com/shriyanshkush/
- **Email:** shriyanshk133@gmail.com  

---

## 📸 Screenshots
1. _Browsing popular media_  
2. _Search functionality_  
3. _Favorite media_  
4. _Media download screen_  
5. _Viewing downloaded media_
