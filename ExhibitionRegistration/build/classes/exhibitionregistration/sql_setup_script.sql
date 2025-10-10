-- ============================================================================
-- VUE_Exhibition.accdb - SALSA Dance Festival Database Setup
-- Victoria University Guild Office
-- ============================================================================

-- STEP 1: Create Participants Table
-- Execute this first in Access Query (SQL View)
-- ============================================================================

CREATE TABLE Participants (
    RegistrationID TEXT(20) CONSTRAINT PK_Participants PRIMARY KEY,
    ParticipantName TEXT(100) NOT NULL,
    Department TEXT(100) NOT NULL,
    DancingPartner TEXT(100),
    ContactNumber TEXT(20) NOT NULL,
    EmailAddress TEXT(100) NOT NULL,
    UniversityIDImage OLEOBJECT
);

-- ============================================================================
-- STEP 2: Insert Sample Data (Execute each INSERT separately)
-- ============================================================================

-- Sample Record 1: Computer Science Student
INSERT INTO Participants 
    (RegistrationID, ParticipantName, Department, DancingPartner, ContactNumber, EmailAddress)
VALUES 
    ('REG2025001', 'Emmanuel Okello', 'Computer Science & IT', 'Grace Namuli', '+256-700-123456', 'e.okello@vu.ac.ug');

-- Sample Record 2: Business Student
INSERT INTO Participants 
    (RegistrationID, ParticipantName, Department, DancingPartner, ContactNumber, EmailAddress)
VALUES 
    ('REG2025002', 'Sarah Achieng', 'Business & Management', 'David Ssemakula', '+256-782-345678', 's.achieng@vu.ac.ug');

-- Sample Record 3: Engineering Student
INSERT INTO Participants 
    (RegistrationID, ParticipantName, Department, DancingPartner, ContactNumber, EmailAddress)
VALUES 
    ('REG2025003', 'James Mugisha', 'Engineering', 'Linda Nakato', '+256-701-987654', 'j.mugisha@vu.ac.ug');

-- Sample Record 4: Arts Student
INSERT INTO Participants 
    (RegistrationID, ParticipantName, Department, DancingPartner, ContactNumber, EmailAddress)
VALUES 
    ('REG2025004', 'Rebecca Atim', 'Arts & Humanities', 'Peter Wanyama', '+256-773-246801', 'r.atim@vu.ac.ug');

-- Sample Record 5: Health Sciences Student
INSERT INTO Participants 
    (RegistrationID, ParticipantName, Department, DancingPartner, ContactNumber, EmailAddress)
VALUES 
    ('REG2025005', 'Kenneth Odongo', 'Health Sciences', 'Mary Nabwire', '+256-704-567890', 'k.odongo@vu.ac.ug');

-- Sample Record 6: Law Student (No Partner)
INSERT INTO Participants 
    (RegistrationID, ParticipantName, Department, DancingPartner, ContactNumber, EmailAddress)
VALUES 
    ('REG2025006', 'Patricia Nakimuli', 'Law', NULL, '+256-712-334455', 'p.nakimuli@vu.ac.ug');

-- Sample Record 7: Education Student
INSERT INTO Participants 
    (RegistrationID, ParticipantName, Department, DancingPartner, ContactNumber, EmailAddress)
VALUES 
    ('REG2025007', 'Isaac Tumwesigye', 'Education', 'Florence Auma', '+256-755-778899', 'i.tumwesigye@vu.ac.ug');

-- ============================================================================
-- STEP 3: Verification Queries
-- ============================================================================

-- Count total participants
SELECT COUNT(*) AS TotalParticipants FROM Participants;

-- View all records
SELECT * FROM Participants ORDER BY RegistrationID;

-- View participants by department
SELECT Department, COUNT(*) AS ParticipantCount 
FROM Participants 
GROUP BY Department 
ORDER BY ParticipantCount DESC;

-- View participants without dancing partners
SELECT RegistrationID, ParticipantName, Department 
FROM Participants 
WHERE DancingPartner IS NULL;

-- View complete participant details
SELECT 
    RegistrationID AS [Reg ID],
    ParticipantName AS [Name],
    Department,
    DancingPartner AS [Partner],
    ContactNumber AS [Contact],
    EmailAddress AS [Email]
FROM Participants
ORDER BY RegistrationID;

-- ============================================================================
-- STEP 4: Data Validation Queries (Optional)
-- ============================================================================

-- Check for duplicate Registration IDs (should return 0 rows)
SELECT RegistrationID, COUNT(*) AS Duplicates
FROM Participants
GROUP BY RegistrationID
HAVING COUNT(*) > 1;

-- Check for invalid email formats (basic check)
SELECT RegistrationID, ParticipantName, EmailAddress
FROM Participants
WHERE EmailAddress NOT LIKE '%@%.%';

-- Check for empty required fields
SELECT * FROM Participants
WHERE ParticipantName IS NULL 
   OR Department IS NULL 
   OR ContactNumber IS NULL 
   OR EmailAddress IS NULL;

-- ============================================================================
-- STEP 5: Useful Management Queries
-- ============================================================================

-- Search participant by Registration ID
SELECT * FROM Participants WHERE RegistrationID = 'REG2025001';

-- Search participants by name (partial match)
SELECT * FROM Participants WHERE ParticipantName LIKE '%Okello%';

-- List participants from specific department
SELECT * FROM Participants WHERE Department = 'Computer Science & IT';

-- List all dancing pairs
SELECT 
    p1.ParticipantName AS Participant1,
    p1.DancingPartner AS Participant2,
    p1.Department
FROM Participants p1
WHERE p1.DancingPartner IS NOT NULL
ORDER BY p1.Department;

-- ============================================================================
-- STEP 6: Update and Delete Examples (Use with caution!)
-- ============================================================================

-- Update contact number for a participant
-- UPDATE Participants 
-- SET ContactNumber = '+256-700-999888' 
-- WHERE RegistrationID = 'REG2025001';

-- Update dancing partner
-- UPDATE Participants 
-- SET DancingPartner = 'New Partner Name' 
-- WHERE RegistrationID = 'REG2025002';

-- Delete a participant (use with extreme caution!)
-- DELETE FROM Participants WHERE RegistrationID = 'REG2025999';

-- ============================================================================
-- STEP 7: Backup Query (Export data)
-- ============================================================================

-- Select all data for export/backup
SELECT 
    RegistrationID,
    ParticipantName,
    Department,
    DancingPartner,
    ContactNumber,
    EmailAddress
INTO Participants_Backup
FROM Participants;

-- ============================================================================
-- STEP 8: Statistics Queries
-- ============================================================================

-- Department participation statistics
SELECT 
    Department,
    COUNT(*) AS TotalParticipants,
    SUM(CASE WHEN DancingPartner IS NOT NULL THEN 1 ELSE 0 END) AS WithPartners,
    SUM(CASE WHEN DancingPartner IS NULL THEN 1 ELSE 0 END) AS WithoutPartners
FROM Participants
GROUP BY Department
ORDER BY TotalParticipants DESC;

-- Email domain statistics
SELECT 
    RIGHT(EmailAddress, LEN(EmailAddress) - INSTR(EmailAddress, '@')) AS EmailDomain,
    COUNT(*) AS UserCount
FROM Participants
GROUP BY RIGHT(EmailAddress, LEN(EmailAddress) - INSTR(EmailAddress, '@'));

-- ============================================================================
-- NOTES:
-- ============================================================================
-- 1. Execute CREATE TABLE first (only once)
-- 2. Execute each INSERT statement separately
-- 3. Images must be added manually through Access interface:
--    - Open table in Datasheet View
--    - Right-click UniversityIDImage field
--    - Insert Object → Create from File → Browse to image
-- 4. Always backup database before running UPDATE or DELETE queries
-- 5. Keep RegistrationID unique (Primary Key constraint enforces this)
-- ============================================================================

-- End of SQL Script