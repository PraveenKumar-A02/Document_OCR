Document OCR Project
A web-based application for extracting text from images using Optical Character Recognition (OCR) and translating it into multiple languages. This project is designed to process handwritten, faded, or old documents, leveraging preprocessing techniques to enhance OCR accuracy and Googletrans for free translation.
Table of Contents

Features
Technologies Used
Prerequisites
Setup Instructions
Usage
Project Structure
Database Schema
Contributing
License
Acknowledgements

Features

OCR Processing: Extracts text from images using Tesseract OCR, with preprocessing for handwritten or faded documents.
Translation: Translates extracted text into multiple languages (English, Tamil, Telugu, Hindi, Japanese) using Googletrans.
Database Storage: Stores original and translated text, along with metadata (filename, languages, upload date), in a MySQL database.
Web Interface: User-friendly frontend for uploading images, selecting languages, and viewing results.
REST API: Backend API for processing documents and managing stored data.
Cross-Platform: Runs on Windows, with paths configurable for other systems.

Technologies Used

Backend:
Java 17
Spring Boot 3.x
Spring Data JPA
MySQL 8.0


Frontend:
HTML, CSS, JavaScript


Python Processing:
Python 3.8+
Tesseract OCR
OpenCV
Googletrans
Pillow
NumPy


Database:
MySQL


Other:
Maven (for Java dependencies)
pip (for Python dependencies)



Prerequisites

Java 17 or higher
Python 3.8 or higher
MySQL 8.0 or higher
Tesseract OCR (installed with executable path configured)
Maven (for building the Java backend)
Git (for cloning the repository)
Internet connection (for Googletrans translation)

Setup Instructions
1. Clone the Repository
git clone https://github.com/your-username/document-ocr-project.git
cd document-ocr-project

2. Set Up the Database

Install MySQL and start the server.

Create a database named document_ocr:
CREATE DATABASE document_ocr;


Run the database initialization script:
mysql -u root -p document_ocr < database/init.sql

Replace root and the password (Mysql@02 in the default configuration) as needed.


3. Install Tesseract OCR

Download and install Tesseract OCR from https://github.com/UB-Mannheim/tesseract/wiki.

Add Tesseract to your system PATH or update the path in python-ocr/ocr_script.py:
pytesseract.pytesseract.tesseract_cmd = r"C:\Program Files\Tesseract-OCR\tesseract.exe"



4. Set Up the Python Environment

Navigate to the python-ocr directory:
cd python-ocr


Install Python dependencies:
pip install -r requirements.txt



5. Configure the Backend

Navigate to the backend directory:
cd backend


Update src/main/resources/application.properties with your MySQL credentials and paths:
spring.datasource.username=root
spring.datasource.password=Mysql@02
python.script.path=C:/Users/your-username/Downloads/praveen/Final_Year_Project/document-ocr-project/python-ocr/ocr_script.py
upload.temp.dir=C:/Users/your-username/Downloads/praveen/Final_Year_Project/document-ocr-project/uploads

Replace your-username with your actual Windows username or adjust paths for your system.

Build and run the Spring Boot application:
mvn clean install
mvn spring-boot:run



6. Serve the Frontend

The frontend is a static index.html file located in the frontend directory.

Serve it using a simple HTTP server (e.g., Python’s http.server):
cd frontend
python -m http.server 8000


Open http://localhost:8000 in your browser.


Usage

Access the Web Interface:
Navigate to http://localhost:8000 (or the port you chose for the frontend server).


Upload a Document:
Select an image file (e.g., JPG, PNG) containing text.
Choose the source language (e.g., English, Tamil) and target language for translation.
Click "Process Document".


View Results:
The original extracted text and its translation will appear below the form.


Check Database:
Query the documents table to verify stored data:
SELECT * FROM document_ocr.documents;





Project Structure
document-ocr-project/
├── backend/                      # Spring Boot backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/documentocr/
│   │   │   │   ├── controller/   # REST controllers
│   │   │   │   ├── entity/       # JPA entities
│   │   │   │   ├── repository/   # JPA repositories
│   │   │   │   └── DocumentOcrApplication.java
│   │   │   └── resources/
│   │   │       └── application.properties
│   └── pom.xml
├── frontend/                     # Static HTML frontend
│   └── index.html
├── python-ocr/                   # Python OCR and translation scripts
│   ├── ocr_script.py
│   └── requirements.txt
├── database/                     # Database initialization scripts
│   └── init.sql
└── README.md

Database Schema
The documents table stores all relevant data:
CREATE TABLE documents (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    original_filename VARCHAR(255) NOT NULL,
    upload_date DATETIME NOT NULL,
    content TEXT NOT NULL,
    language CHAR(3) NOT NULL,
    translated_content TEXT,
    translated_content_language CHAR(3),
    INDEX idx_language (language)
);

Contributing
Contributions are welcome! To contribute:

Fork the repository.
Create a feature branch (git checkout -b feature/your-feature).
Commit your changes (git commit -m "Add your feature").
Push to the branch (git push origin feature/your-feature).
Open a Pull Request.

Please ensure your code follows the project’s coding style and includes tests where applicable.
License
This project is licensed under the MIT License. See the LICENSE file for details.
Acknowledgements

Tesseract OCR for text extraction.
Googletrans for free translation.
OpenCV for image preprocessing.
Spring Boot for the backend framework.

