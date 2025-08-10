# 📱 Smart Daily Expense Tracker  
*A full-featured expense management module for small business owners*

---

## 📜 Problem Statement
Small business owners often lose track of their daily expenses — scattered on WhatsApp messages, sticky notes, or just forgotten. This leads to poor visibility into **cash flow**.  

The **Smart Daily Expense Tracker** helps digitize expense recording with a clean, fast, and intelligent interface. It enables business owners to **capture, view, analyze, and export** expenses with ease.

---

## 🎯 Features

### **1. Expense Entry Screen**
- 📝 **Fields**: Title, Amount (₹), Category *(Staff, Travel, Food, Utility)*  
- 🗒 Optional: Notes *(max 100 chars)*, Receipt Image *(upload/mock)*  
- 📊 **Live Total Spent Today** display  
- ✅ Validation: Amount > 0, Title non-empty  
- ✨ Smooth animations & toast notifications on add  
- 🔍 Duplicate entry detection *(bonus)*  

---

### **2. Expense List Screen**
- 📅 Default: *Today’s expenses*  
- 🔄 Switch to past dates via calendar/filter  
- 📂 Group view: by category or time  
- 📊 Display: total count, total amount  
- 📭 Empty state for no data  

---

### **3. Expense Report Screen**
- 📈 Mock **7-day report**:
  - Daily totals
  - Category-wise totals  
- 📊 **Charts**: Bar / Line (mocked)  
- 📤 Export: Simulated PDF/CSV + Share Intent  

---

## 🏗️ Architecture & Tech Stack

| Layer | Details |
|-------|---------|
| **UI** | Jetpack Compose / XML *(as per your implementation)* |
| **State Management** | ViewModel + StateFlow *(or LiveData)* |
| **Data** | In-memory Repository / Room *(bonus)* |
| **Navigation** | Jetpack Navigation |
| **Theme** | Light/Dark theme switcher |
| **Persistence** | Room / Datastore *(bonus)* |
| **Offline Support** | Mock offline-first sync *(bonus)* |

---

## 🛠️ Implementation Highlights
- ♻ **Reusable UI components** for consistent design  
- ⚡ **Real-time state updates** using StateFlow  
- 💾 **Local persistence** for offline use *(bonus)*  
- 🎨 Theme toggle with smooth transitions  
- 🎯 Strict input validation for data accuracy  

---

## 📸 Screenshots *(Mock Examples)*

| Expense Entry | Expense List | Expense Report |
|---------------|--------------|----------------|
| ![Entry Screen](docs/entry.png) | ![List Screen](docs/list.png) | ![Report Screen](docs/report.png) |

---

## 🌟 Bonus Features Implemented
- ✅ **Theme switcher** *(Light/Dark)*  
- ✅ **Animation on add**  
- ✅ **Duplicate detection**  
- ✅ **Offline-first mock sync**  

---

## 📌 Future Enhancements
- 🔄 Real backend API integration  
- 📊 Interactive chart views  
- 📌 Multiple currency support  
- 📱 Widget for quick add  

---

## 📜 License
This project is licensed under the **MIT License** — feel free to modify and share.
