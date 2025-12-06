# 🎨 Swing Paint – Java Drawing Board

A Java Swing–based Drawing Board application that allows users to draw freehand lines, shapes, pick colors, and interact with a custom-built canvas. This project is still under development, with several features partially implemented and more improvements planned.

---

## ✨ Current Features
- ✏️ Freehand drawing tool  
- ⭕ Basic shapes (circle, rectangle, triangle)  
- 🎨 Color selection  
- 🖱️ Mouse-based drawing & shape creation  
- 🧩 Custom Swing UI using NetBeans form editor  
- 🖼️ All icons stored under `src/img/`

---

## 🛠️ Features Under Development
The following tasks are **not fully implemented yet**, and will be completed in upcoming updates:

- ❗ **Exception Handling** – Safer user interactions and state handling  
- 🩹 **Eraser Tool** – Ability to remove portions of the drawing  
- 🔁 **Redo Functionality** – Restore undone actions  
- 🧱 **Improved GUI Layout** – Better alignment, spacing, toolbars, and UI polish  
- ⚙️ **Minor Tweaks** – Tool behavior fixes, smoother drawing, code cleanup  

---

## 🚀 How to Run the Application

### 1️⃣ Compile all Java files

javac -d bin src/swingpaint/*.java


### 2️⃣ Run the main class

java -cp bin swingpaint.Swing_Paint


> The main entry point is **Swing_Paint.java** located inside `src/swingpaint/`.

---

## 📁 Project Structure

SwingPaint/
├─ src/
│ ├─ swingpaint/
│ │ ├─ Swing_Paint.java (Main class)
│ │ ├─ Swing_Paint.form
│ │ └─ Colorshape.java
│ └─ img/
│ └─ (All drawing tool icons)
├─ build/
├─ dist/
├─ nbproject/
├─ README.md
├─ .gitignore
└─ manifest.mf


---

## 🎯 Learning Objectives
- Building Java Swing GUI applications  
- Using custom painting with `Graphics` and `Graphics2D`  
- Handling mouse events (`MouseListener`, `MouseMotionListener`)  
- Managing shapes, colors, tools, and UI state  
- Understanding NetBeans auto-generated UI code  
- Incremental development and Git version control  

---

## 🖼️ Screenshot
*(Add your screenshot here later)*  


---

## 👤 Author
**Koushik Karmakar**  
B.Tech Civil Engineering Student  
Learning Java GUI Development & Backend Technologies

---

## 📌 Notes
This project is still evolving. Several tools and UI updates will be added in future commits as part of the learning and improvement process.
