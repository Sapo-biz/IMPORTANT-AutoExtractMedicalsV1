# AutoExtract Medical Services

## Overview
AutoExtract Medical Services is a Java application designed to automate the extraction and structuring of clinical data from unstructured sources, such as scanned documents and audio notes. The goal is to streamline research and charting workflows for clinicians and researchers by converting messy inputs into clean, structured, and analytics-ready data.

## Features
- **PDF/Text Extraction (Simulated):** Reads and parses patient information from a sample text file, simulating the extraction process from a PDF.
- **Structured Data Output:** Extracted data is organized into fields such as patient name, DOB, MRN, diagnosis, medications, allergies, vitals, and notes.
- **Extensible Design:** Ready for future integration with OCR, NLP, and speech-to-text technologies.

## Usage
### Prerequisites
- Java 8 or higher

### How to Run the Demo
1. Clone or download this repository.
2. Ensure the following files are in the same directory:
   - `AutoExtract.java`
   - `patient_example.txt`
3. Compile the Java file:
   ```sh
   javac AutoExtract.java
   ```
4. Run the application:
   ```sh
   java AutoExtract
   ```
5. The application will read `patient_example.txt` and display the extracted patient information in the console.

## Example Input File (`patient_example.txt`)
```
Patient Name: John Doe
DOB: 01/15/1975
MRN: 123456
Diagnosis: Hypertension
Medications: Lisinopril 10mg daily
Allergies: Penicillin
Vitals:
  - BP: 140/90 mmHg
  - HR: 78 bpm
  - Temp: 98.6 F
Notes: Patient reports mild headache, no chest pain or shortness of breath.
```

## Future Development
- **Swing User Interface:** Build a professional desktop UI for data input, review, and export.
- **Real PDF Parsing:** Integrate OCR libraries (e.g., Tesseract, PDFBox) to extract data directly from PDF files.
- **Speech-to-Text Integration:** Add support for audio input and medical NLP tagging.
- **Google Sheets Export:** Enable exporting structured data to Google Sheets for research and charting.
- **Summarization & Alerts:** Use AI models to generate clinical summaries and real-time alerts.

## License
MIT License

## Contact
For questions or collaboration, please contact Jason Ho. 