# Document OCR Project

A web-based application for extracting text from images using Optical Character Recognition (OCR) and translating it into multiple languages. This project processes handwritten, faded, or old documents by using preprocessing techniques to enhance OCR accuracy, and Googletrans for free translation.

---

## Table of Contents
- Features
- Technologies Used
- Prerequisites
- Setup Instructions
- Usage
- Project Structure
- Database Schema
- Contributing
- License
- Acknowledgements

---

## Features
- **OCR Processing:** Extracts text from images using Tesseract OCR with preprocessing for improved accuracy.
- **Translation:** Supports multiple languages including English, Tamil, Telugu, Hindi, and Japanese using Googletrans.
- **Database Storage:** Stores original and translated text along with metadata (filename, language, upload date) in a MySQL database.
- **Web Interface:** User-friendly interface for uploading images, selecting languages, and viewing results.
- **REST API:** Backend API for handling document processing and data management.
- **Cross-Platform Support:** Primarily supports Windows, with configurable paths for other systems.

---

## Technologies Used

### Backend
- Java 17
- Spring Boot 3.x
- Spring Data JPA
- MySQL 8.0

### Frontend
- HTML, CSS, JavaScript

### Python Processing
- Python 3.8+
- Tesseract OCR
- OpenCV
- Googletrans
- Pillow
- NumPy

### Database
- MySQL

### Other Tools
- Maven (Java dependencies)
- pip (Python dependencies)

---

## Prerequisites
- Java 17 or higher
- Python 3.8 or higher
- MySQL 8.0 or higher
- Tesseract OCR installed and configured
- Maven
- Git
- Internet connection for translation service

---

## Setup Instructions

### 1. Clone the Repository
```bash
git clone https://github.com/your-username/document-ocr-project.git
cd document-ocr-project
```

### 2. Set Up the Database
```sql
CREATE DATABASE document_ocr;
```
Run the init script:
```bash
mysql -u root -p document_ocr < database/init.sql
```
Update credentials as needed.

### 3. Install Tesseract OCR
Download from [Tesseract GitHub](https://github.com/UB-Mannheim/tesseract/wiki) and add the executable path:
```python
pytesseract.pytesseract.tesseract_cmd = r"C:\Program Files\Tesseract-OCR\tesseract.exe"
```

### 4. Set Up the Python Environment
```bash
cd python-ocr
pip install -r requirements.txt
```

### 5. Configure the Backend
```bash
cd backend
```
Edit `application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=Mysql@02
python.script.path=C:/path/to/python-ocr/ocr_script.py
upload.temp.dir=C:/path/to/uploads
```
Build and run the application:
```bash
mvn clean install
mvn spring-boot:run
```

### 6. Serve the Frontend
```bash
cd frontend
python -m http.server 8000
```
Open [http://localhost:8000](http://localhost:8000) in your browser.

---

## Usage
1. Open the Web Interface.
2. Upload an image (JPG, PNG).
3. Choose source and target languages.
4. Click "Process Document".
5. View original and translated text.
6. Check stored data:
```sql
SELECT * FROM document_ocr.documents;
```

---

## Project Structure
```
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
```

---

## Database Schema
```sql
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
```

---

## Contributing
1. Fork the repository.
2. Create a feature branch:
```bash
git checkout -b feature/your-feature
```
3. Commit your changes:
```bash
git commit -m "Add your feature"
```
4. Push to your branch:
```bash
git push origin feature/your-feature
```
5. Open a Pull Request.

Please ensure your code follows the project style and includes tests if applicable.

---

## License
This project is licensed under the [MIT License](LICENSE).

---

## Acknowledgements
- Tesseract OCR for text extraction
- Googletrans for translation
- OpenCV for image preprocessing
- Spring Boot for backend framework

