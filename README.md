# AutoExtract Medical Services

## Overview
AutoExtract Medical Services is a Java application that automatically extracts and structures clinical data from medical PDFs and other unstructured sources. Designed for healthcare professionals and researchers, it transforms complex medical documents into organized, analytics-ready data with 90%+ accuracy on standard H&P formats.

## Key Features
- **Smart PDF Extraction:** Real PDF parsing using Apache PDFBox to process medical histories, physical exams, and clinical notes
- **Comprehensive Data Capture:** Extracts 20+ clinical fields including:
  - Patient demographics (name, DOB, MRN, age, gender)
  - Medical history (diagnoses, medications, allergies)
  - Clinical findings (vitals, physical exam, problem lists)
  - Assessment and plan
- **Intelligent Summary Generation:** Auto-creates organized clinical summaries
- **One-Click Export:** Copy structured data to clipboard for EMR integration
- **Responsive GUI:** Professional Swing interface for easy review and validation

## Technical Highlights
- Maven-based Java application
- Apache PDFBox for advanced PDF text extraction
- Regular expression-based field mapping
- Modular architecture for easy expansion

## Installation & Usage

### Prerequisites
- Java 8+ JDK
- Maven 3.6+

### Quick Start

# Clone repository

# Build and run
```sh
mvn clean package
java -jar target/autoextract-1.0-jar-with-dependencies.jar
```

# Running with Maven:
```sh
mvn compile exec:java
```

### Roadmap

Next Release (v2.0)

Google Sheets API integration
Speech-to-text for audio notes
Custom template support
HL7/FHIR export capabilities
Future Development

AI-powered data validation
EMR direct connect (Epic, Cerner)
Mobile clinician app

## License

This project is currently unlicensed. For usage rights, please contact the author.

## Contact
send an email via sapo.business.ventures@gmail.com

