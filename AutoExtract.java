package com.yourcompany.autoextract;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.util.regex.*;

public class AutoExtract {
    // Enhanced PDF extraction for medical records
    public StructuredData extractFromPDF(String filePath) {
        StructuredData data = new StructuredData();
        try (PDDocument document = PDDocument.load(new File(filePath))) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            
            // Extract patient demographics
            extractPatientInfo(data, text);
            
            // Extract medical history
            extractMedicalHistory(data, text);
            
            // Extract vitals and exam findings
            extractVitalsAndExam(data, text);
            
            // Extract problem list and assessment
            extractProblemsAndAssessment(data, text);
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, 
                "Error reading PDF file: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return data;
    }

    private void extractPatientInfo(StructuredData data, String text) {
        // Extract using both patterns and text markers
        Pattern pattern = Pattern.compile("Patient is a (\\d+) year-old (.+?) (male|female)");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            data.age = matcher.group(1);
            data.gender = matcher.group(3);
        }
        
        // Chief Complaint
        extractField(data, "chiefComplaint", text, "Chief Complaint: \"(.+?)\"");
        
        // Basic demographics
        extractField(data, "patientName", text, "Patient Name: (.+)");
        extractField(data, "dob", text, "DOB: (.+)");
        extractField(data, "mrn", text, "MRN: (.+)");
    }

    private void extractMedicalHistory(StructuredData data, String text) {
        // Past Medical History
        extractField(data, "pastMedicalHistory", text, "Past Medical History:(.+?)Surgical History:", Pattern.DOTALL);
        
        // Medications
        extractField(data, "medications", text, "Medications:(.+?)Allergies:", Pattern.DOTALL);
        
        // Allergies
        extractField(data, "allergies", text, "Allergies:(.+?)Family History:", Pattern.DOTALL);
        
        // Family History
        extractField(data, "familyHistory", text, "Family History:(.+?)Social History:", Pattern.DOTALL);
        
        // Social History
        extractField(data, "socialHistory", text, "Social History:(.+?)Review of Systems:", Pattern.DOTALL);
    }

    private void extractVitalsAndExam(StructuredData data, String text) {
        // Vitals
        extractField(data, "vitals", text, "Vitals:(.+?)General Appearance:", Pattern.DOTALL);
        
        // Physical Exam
        extractField(data, "physicalExam", text, "Physical Exam:(.+?)Pertinent Diagnostic Tests:", Pattern.DOTALL);
        
        // Specific vital signs
        extractField(data, "bp", text, "BP (\\d+/\\d+)");
        extractField(data, "hr", text, "HR (\\d+)");
        extractField(data, "temp", text, "T ([\\d.]+)");
        extractField(data, "rr", text, "RR (\\d+)");
    }

    private void extractProblemsAndAssessment(StructuredData data, String text) {
        // Problem List
        extractField(data, "problemList", text, "Problem List:(.+?)Summary Statement:", Pattern.DOTALL);
        
        // Assessment and Plan
        extractField(data, "assessmentPlan", text, "Assessment and Plan:(.+?)Sources:", Pattern.DOTALL);
        
        // Diagnosis
        extractField(data, "diagnosis", text, "Diagnosis:(.+?)\\n\\n", Pattern.DOTALL);
    }

    private void extractField(StructuredData data, String field, String text, String regex) {
        extractField(data, field, text, regex, 0);
    }

    private void extractField(StructuredData data, String field, String text, String regex, int flags) {
        Pattern pattern = Pattern.compile(regex, flags);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            String value = matcher.group(1).trim()
                .replaceAll("\\n", " ")  // Replace newlines with spaces
                .replaceAll("\\s+", " "); // Collapse multiple spaces
            
            switch (field) {
                case "patientName": data.patientName = value; break;
                case "dob": data.dob = value; break;
                case "mrn": data.mrn = value; break;
                case "age": data.age = value; break;
                case "gender": data.gender = value; break;
                case "chiefComplaint": data.chiefComplaint = value; break;
                case "pastMedicalHistory": data.pastMedicalHistory = value; break;
                case "medications": data.medications = value; break;
                case "allergies": data.allergies = value; break;
                case "familyHistory": data.familyHistory = value; break;
                case "socialHistory": data.socialHistory = value; break;
                case "vitals": data.vitals = value; break;
                case "physicalExam": data.physicalExam = value; break;
                case "bp": data.bp = value; break;
                case "hr": data.hr = value; break;
                case "temp": data.temp = value; break;
                case "rr": data.rr = value; break;
                case "problemList": data.problemList = value; break;
                case "assessmentPlan": data.assessmentPlan = value; break;
                case "diagnosis": data.diagnosis = value; break;
            }
        }
    }

    // Auto-Generate Summaries
    public String generateSummary(StructuredData data, String context) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== PATIENT SUMMARY ===\n");
        sb.append("Name: ").append(data.patientName != null ? data.patientName : "N/A").append("\n");
        sb.append("Age/Gender: ").append(data.age != null ? data.age : "N/A").append("/")
          .append(data.gender != null ? data.gender : "N/A").append("\n");
        sb.append("MRN: ").append(data.mrn != null ? data.mrn : "N/A").append("\n");
        sb.append("DOB: ").append(data.dob != null ? data.dob : "N/A").append("\n\n");
        
        sb.append("=== CHIEF COMPLAINT ===\n");
        sb.append(data.chiefComplaint != null ? data.chiefComplaint : "N/A").append("\n\n");
        
        sb.append("=== DIAGNOSIS ===\n");
        sb.append(data.diagnosis != null ? data.diagnosis : "N/A").append("\n\n");
        
        sb.append("=== ACTIVE PROBLEMS ===\n");
        sb.append(data.problemList != null ? data.problemList : "N/A").append("\n\n");
        
        sb.append("=== VITALS ===\n");
        sb.append("BP: ").append(data.bp != null ? data.bp : "N/A").append("\n");
        sb.append("HR: ").append(data.hr != null ? data.hr : "N/A").append("\n");
        sb.append("Temp: ").append(data.temp != null ? data.temp : "N/A").append("\n");
        sb.append("RR: ").append(data.rr != null ? data.rr : "N/A").append("\n\n");
        
        sb.append("=== ACTIVE MEDICATIONS ===\n");
        sb.append(data.medications != null ? data.medications : "N/A").append("\n\n");
        
        sb.append("=== ALLERGIES ===\n");
        sb.append(data.allergies != null ? data.allergies : "N/A").append("\n\n");
        
        sb.append("=== ASSESSMENT & PLAN ===\n");
        sb.append(data.assessmentPlan != null ? data.assessmentPlan : "N/A").append("\n");
        
        return sb.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AutoExtract().createAndShowGUI());
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("AutoExtract Medical Services");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // Header
        JLabel titleLabel = new JLabel("AutoExtract Medical Services", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        frame.add(titleLabel, BorderLayout.NORTH);

        // Center panel for file input and table
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel fileLabel = new JLabel("Enter patient PDF file name:");
        JTextField fileField = new JTextField("Sample-Adult-History-And-Physical-By-M2-Student.pdf", 30);
        JButton loadButton = new JButton("Load & Extract");
        inputPanel.add(fileLabel);
        inputPanel.add(fileField);
        inputPanel.add(loadButton);
        centerPanel.add(inputPanel, BorderLayout.NORTH);

        // Table for displaying extracted data
        String[] columnNames = {"Field", "Value"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(20);
        JScrollPane scrollPane = new JScrollPane(table);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        // Copy to Clipboard button
        JButton copyButton = new JButton("Copy to Clipboard");
        buttonPanel.add(copyButton);

        // Generate Summary button
        JButton summaryButton = new JButton("Generate Summary");
        buttonPanel.add(summaryButton);
        
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(centerPanel, BorderLayout.CENTER);

        // Footer
        JLabel footerLabel = new JLabel("© 2025 AutoExtract Medical Services | For demo use only", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        footerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        frame.add(footerLabel, BorderLayout.SOUTH);

        // Button actions
        loadButton.addActionListener(e -> {
            String filename = fileField.getText().trim();
            if (!filename.toLowerCase().endsWith(".pdf")) {
                JOptionPane.showMessageDialog(frame, 
                    "Please specify a PDF file (.pdf extension)", 
                    "Invalid File Type", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            try {
                StructuredData data = extractFromPDF(filename);
                tableModel.setRowCount(0); // Clear previous data
                
                // Add all extracted data to the table
                addTableRow(tableModel, "Patient Name", data.patientName);
                addTableRow(tableModel, "DOB", data.dob);
                addTableRow(tableModel, "MRN", data.mrn);
                addTableRow(tableModel, "Age", data.age);
                addTableRow(tableModel, "Gender", data.gender);
                addTableRow(tableModel, "Chief Complaint", data.chiefComplaint);
                addTableRow(tableModel, "Diagnosis", data.diagnosis);
                addTableRow(tableModel, "Past Medical History", data.pastMedicalHistory);
                addTableRow(tableModel, "Medications", data.medications);
                addTableRow(tableModel, "Allergies", data.allergies);
                addTableRow(tableModel, "Family History", data.familyHistory);
                addTableRow(tableModel, "Social History", data.socialHistory);
                addTableRow(tableModel, "Vitals", data.vitals);
                addTableRow(tableModel, "BP", data.bp);
                addTableRow(tableModel, "HR", data.hr);
                addTableRow(tableModel, "Temp", data.temp);
                addTableRow(tableModel, "RR", data.rr);
                addTableRow(tableModel, "Physical Exam", data.physicalExam);
                addTableRow(tableModel, "Problem List", data.problemList);
                addTableRow(tableModel, "Assessment & Plan", data.assessmentPlan);
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, 
                    "Error processing PDF: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        // Copy to Clipboard action
        copyButton.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                sb.append(tableModel.getValueAt(i, 0)).append("\t")
                  .append(tableModel.getValueAt(i, 1)).append("\n");
            }
            StringSelection selection = new StringSelection(sb.toString());
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
            JOptionPane.showMessageDialog(frame, 
                "Patient data copied to clipboard!\nPaste directly into Google Sheets.", 
                "Copied", JOptionPane.INFORMATION_MESSAGE);
        });

        // Generate Summary action
        summaryButton.addActionListener(e -> {
            StructuredData data = new StructuredData();
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String field = (String) tableModel.getValueAt(i, 0);
                String value = (String) tableModel.getValueAt(i, 1);
                
                // Map all fields from table to StructuredData object
                switch (field) {
                    case "Patient Name": data.patientName = value; break;
                    case "DOB": data.dob = value; break;
                    case "MRN": data.mrn = value; break;
                    case "Age": data.age = value; break;
                    case "Gender": data.gender = value; break;
                    case "Chief Complaint": data.chiefComplaint = value; break;
                    case "Diagnosis": data.diagnosis = value; break;
                    case "Past Medical History": data.pastMedicalHistory = value; break;
                    case "Medications": data.medications = value; break;
                    case "Allergies": data.allergies = value; break;
                    case "Family History": data.familyHistory = value; break;
                    case "Social History": data.socialHistory = value; break;
                    case "Vitals": data.vitals = value; break;
                    case "BP": data.bp = value; break;
                    case "HR": data.hr = value; break;
                    case "Temp": data.temp = value; break;
                    case "RR": data.rr = value; break;
                    case "Physical Exam": data.physicalExam = value; break;
                    case "Problem List": data.problemList = value; break;
                    case "Assessment & Plan": data.assessmentPlan = value; break;
                }
            }
            
            String summary = generateSummary(data, "rounds");
            JTextArea textArea = new JTextArea(summary, 20, 60);
            textArea.setWrapStyleWord(true);
            textArea.setLineWrap(true);
            textArea.setEditable(false);
            
            JScrollPane summaryScrollPane = new JScrollPane(textArea);
            int option = JOptionPane.showOptionDialog(frame, summaryScrollPane, "Patient Summary", 
                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, 
                null, new Object[]{"Copy", "Close"}, "Copy");
                
            if (option == 0) {
                StringSelection selection = new StringSelection(summary);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
            }
        });

        frame.setVisible(true);
    }
    
    private void addTableRow(DefaultTableModel model, String field, String value) {
        if (value != null && !value.trim().isEmpty()) {
            model.addRow(new Object[]{field, value});
        }
    }
}

class StructuredData {
    // Patient Demographics
    String patientName;
    String dob;
    String mrn;
    String age;
    String gender;
    
    // Medical Information
    String chiefComplaint;
    String pastMedicalHistory;
    String medications;
    String allergies;
    String familyHistory;
    String socialHistory;
    String diagnosis;
    
    // Clinical Data
    String vitals;
    String bp;
    String hr;
    String temp;
    String rr;
    String physicalExam;
    
    // Assessment
    String problemList;
    String assessmentPlan;
}
