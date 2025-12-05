# 🎯 Complete Project Demo Guide

This guide will help you demonstrate **all features** of your File Management System including synchronous operations, asynchronous processing, and threading capabilities.

---

## 📋 **Pre-Demo Setup**

### 1. Start the Application
```bash
# Clean and start fresh
rm -rf storage/
./mvnw clean spring-boot:run
```

### 2. Verify Application is Running
```bash
# Should see: "Started JpApplication in X seconds"
# Access: http://localhost:8080
```

### 3. Open Multiple Terminal Windows
- **Terminal 1**: Application logs (running app)
- **Terminal 2**: API testing commands
- **Terminal 3**: File system monitoring (optional)

```bash
# Terminal 3: Watch file system changes in real-time
watch -n 1 'ls -lR storage/'
```

---

## 🎬 **Demo Script - Follow This Order**

---

## **Part 1: Basic Folder Operations** 📂

### **Demo 1.1: Create Root Folder**

```bash
curl -X POST http://localhost:8080/api/folders \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Documents",
    "parentPath": null
  }'
```

**Expected Response:**
```json
{
  "name": "Documents",
  "path": "Documents",
  "createdAt": "2025-11-28T10:30:00",
  "updatedAt": "2025-11-28T10:30:00"
}
```

**✅ Show:** Open Finder/Explorer → Navigate to `storage/` → See `Documents/` folder created!

---

### **Demo 1.2: Create Nested Folders**

```bash
# Create Photos folder
curl -X POST http://localhost:8080/api/folders \
  -H "Content-Type: application/json" \
  -d '{"name": "Photos", "parentPath": null}'

# Create subfolder inside Documents
curl -X POST http://localhost:8080/api/folders \
  -H "Content-Type: application/json" \
  -d '{"name": "Work", "parentPath": "Documents"}'

# Create deeper nesting
curl -X POST http://localhost:8080/api/folders \
  -H "Content-Type: application/json" \
  -d '{"name": "Projects", "parentPath": "Documents/Work"}'
```

**✅ Show:** File system structure now looks like:
```
storage/
├── Documents/
│   └── Work/
│       └── Projects/
└── Photos/
```

---

### **Demo 1.3: Get Root Contents**

```bash
curl http://localhost:8080/api/folders/root
```

**Expected Response:**
```json
{
  "folders": [
    {
      "name": "Documents",
      "path": "Documents",
      "createdAt": "2025-11-28T10:30:00"
    },
    {
      "name": "Photos",
      "path": "Photos",
      "createdAt": "2025-11-28T10:31:00"
    }
  ],
  "files": []
}
```

---

## **Part 2: File Upload Operations** 📄

### **Demo 2.1: Create Test Files First**

```bash
# Create sample files for demo
echo "This is a test document" > test-document.txt
echo "Meeting notes for project" > notes.txt
echo "Important data" > data.csv

# Create a larger file to show progress
dd if=/dev/zero of=large-file.bin bs=1M count=10
```

---

### **Demo 2.2: Upload Text File**

```bash
curl -X POST http://localhost:8080/api/files/upload \
  -F "file=@test-document.txt" \
  -F "folderPath=Documents"
```

**Expected Response:**
```json
{
  "name": "test-document.txt",
  "path": "Documents/test-document.txt",
  "size": 24,
  "mimeType": "text/plain",
  "fileTypeCategory": "DOCUMENT",
  "fileTypeDescription": "Text Document",
  "extension": "txt",
  "createdAt": "2025-11-28T10:35:00"
}
```

**✅ Show:** Open `storage/Documents/test-document.txt` → File is there with actual content!

---

### **Demo 2.3: Upload Multiple Files to Different Folders**

```bash
# Upload to Documents/Work
curl -X POST http://localhost:8080/api/files/upload \
  -F "file=@notes.txt" \
  -F "folderPath=Documents/Work"

# Upload to Photos
curl -X POST http://localhost:8080/api/files/upload \
  -F "file=@vacation.jpg" \
  -F "folderPath=Photos"

# Upload to root
curl -X POST http://localhost:8080/api/files/upload \
  -F "file=@data.csv" \
  -F "folderPath="
```

---

### **Demo 2.4: Show File Type Detection (Inheritance)**

```bash
# Upload different file types to show polymorphism
curl -X POST http://localhost:8080/api/files/upload \
  -F "file=@document.pdf" \
  -F "folderPath=Documents"

curl -X POST http://localhost:8080/api/files/upload \
  -F "file=@photo.jpg" \
  -F "folderPath=Photos"

curl -X POST http://localhost:8080/api/files/upload \
  -F "file=@song.mp3" \
  -F "folderPath=Music"
```

**Expected Response Shows Different Types:**
```json
// PDF
{
  "fileTypeCategory": "DOCUMENT",
  "fileTypeDescription": "PDF Document"
}

// JPG
{
  "fileTypeCategory": "IMAGE",
  "fileTypeDescription": "JPEG Image"
}

// MP3
{
  "fileTypeCategory": "AUDIO",
  "fileTypeDescription": "MP3 Audio"
}
```

**✅ Explain:** "This demonstrates **inheritance** - each file type (DocumentFile, ImageFile, AudioFile) extends the base FileItem class!"

---

## **Part 3: File Download & Retrieval** ⬇️

### **Demo 3.1: Download File**

```bash
curl -OJ "http://localhost:8080/api/files/download?path=Documents/test-document.txt"
```

**Expected:** File downloads as `test-document.txt`

**✅ Show:** Open downloaded file → Same content as original!

---

### **Demo 3.2: View Folder Contents**

```bash
curl "http://localhost:8080/api/folders/contents?path=Documents"
```

**Expected Response:**
```json
{
  "folders": [
    {
      "name": "Work",
      "path": "Documents/Work"
    }
  ],
  "files": [
    {
      "name": "document.pdf",
      "path": "Documents/document.pdf",
      "size": 50000,
      "fileTypeCategory": "DOCUMENT"
    },
    {
      "name": "test-document.txt",
      "path": "Documents/test-document.txt",
      "size": 24
    }
  ]
}
```

---

## **Part 4: Async Upload (Threading Demo)** 🧵

### **Demo 4.1: Single Async Upload**

```bash
# Upload file asynchronously
RESPONSE=$(curl -X POST http://localhost:8080/api/async/files/upload \
  -F "file=@large-file.bin" \
  -F "folderPath=Documents")

echo $RESPONSE
```

**Expected Response (Immediate):**
```json
{
  "taskId": "550e8400-e29b-41d4-a716-446655440000",
  "message": "File upload started",
  "statusUrl": "/api/async/files/status/550e8400-e29b-41d4-a716-446655440000"
}
```

**✅ Explain:** "Notice the API returned **immediately**! The file is uploading in the background using a separate thread!"

---

### **Demo 4.2: Check Upload Status (Polling)**

```bash
# Extract taskId from previous response
TASK_ID="550e8400-e29b-41d4-a716-446655440000"

# Poll status multiple times
curl "http://localhost:8080/api/async/files/status/$TASK_ID"
```

**Expected Responses (Shows Progress):**

**First Check (In Progress):**
```json
{
  "taskId": "550e8400-e29b-41d4-a716-446655440000",
  "status": "IN_PROGRESS",
  "progressPercent": 45,
  "message": "Uploading file...",
  "startTime": "2025-11-28T10:40:00",
  "fileInfo": null
}
```

**Second Check (Completed):**
```json
{
  "taskId": "550e8400-e29b-41d4-a716-446655440000",
  "status": "COMPLETED",
  "progressPercent": 100,
  "message": "File uploaded successfully",
  "startTime": "2025-11-28T10:40:00",
  "endTime": "2025-11-28T10:40:05",
  "durationMs": 5000,
  "fileInfo": {
    "name": "large-file.bin",
    "path": "Documents/large-file.bin",
    "size": 10485760
  }
}
```

**✅ Show Application Logs:** Point out the thread pool executor logs showing async processing!

---

### **Demo 4.3: Batch Upload (Parallel Threading)**

```bash
# Create multiple test files
for i in {1..5}; do
  echo "Test file $i" > "test-file-$i.txt"
done

# Upload all files in parallel
curl -X POST http://localhost:8080/api/async/files/batch-upload \
  -F "files=@test-file-1.txt" \
  -F "files=@test-file-2.txt" \
  -F "files=@test-file-3.txt" \
  -F "files=@test-file-4.txt" \
  -F "files=@test-file-5.txt" \
  -F "folderPath=Documents/Batch"
```

**Expected Response:**
```json
{
  "totalFiles": 5,
  "taskIds": {
    "test-file-1.txt": "task-id-1",
    "test-file-2.txt": "task-id-2",
    "test-file-3.txt": "task-id-3",
    "test-file-4.txt": "task-id-4",
    "test-file-5.txt": "task-id-5"
  },
  "statusUrls": {
    "test-file-1.txt": "/api/async/files/status/task-id-1",
    ...
  }
}
```

**✅ Explain:** "All 5 files are uploading **simultaneously** using multiple threads from the thread pool! This demonstrates **parallel processing**!"

**✅ Show Logs:** Point out multiple threads processing files concurrently:
```
2025-11-28 10:45:00 [file-processor-1] Processing: test-file-1.txt
2025-11-28 10:45:00 [file-processor-2] Processing: test-file-2.txt
2025-11-28 10:45:00 [file-processor-3] Processing: test-file-3.txt
```

---

### **Demo 4.4: Check All Task Statuses**

```bash
# Check status of all uploads
for task_id in task-id-1 task-id-2 task-id-3 task-id-4 task-id-5; do
  curl "http://localhost:8080/api/async/files/status/$task_id"
  echo ""
done
```

---

### **Demo 4.5: Sync Upload (Wait for Completion)**

```bash
# Upload and wait for result
curl -X POST http://localhost:8080/api/async/files/upload-sync \
  -F "file=@important-doc.pdf" \
  -F "folderPath=Documents"
```

**Expected:** Returns complete FileItemDTO after upload finishes (blocks until done)

**✅ Explain:** "This still uses async processing internally, but the API **waits** for completion before responding!"

---

## **Part 5: Delete Operations** 🗑️

### **Demo 5.1: Delete Single File**

```bash
curl -X DELETE "http://localhost:8080/api/files?path=Documents/test-document.txt"
```

**Expected Response:**
```json
{
  "message": "File deleted successfully",
  "path": "Documents/test-document.txt"
}
```

**✅ Show:** File is gone from `storage/Documents/`

---

### **Demo 5.2: Delete Folder (Cascade)**

```bash
# First show folder contents
curl "http://localhost:8080/api/folders/contents?path=Documents/Work"

# Delete folder with all contents
curl -X DELETE "http://localhost:8080/api/folders?path=Documents/Work"
```

**Expected Response:**
```json
{
  "message": "Folder deleted successfully",
  "path": "Documents/Work",
  "deletedFiles": 3,
  "deletedFolders": 1
}
```

**✅ Show:** Entire `Documents/Work/` directory and all files inside are deleted!

---

## **Part 6: Complete Workflow Demo** 🔄

### **Demo 6: End-to-End Scenario**

```bash
# 1. Create project structure
curl -X POST http://localhost:8080/api/folders \
  -H "Content-Type: application/json" \
  -d '{"name": "MyProject", "parentPath": null}'

curl -X POST http://localhost:8080/api/folders \
  -H "Content-Type: application/json" \
  -d '{"name": "src", "parentPath": "MyProject"}'

curl -X POST http://localhost:8080/api/folders \
  -H "Content-Type: application/json" \
  -d '{"name": "docs", "parentPath": "MyProject"}'

# 2. Upload files
curl -X POST http://localhost:8080/api/files/upload \
  -F "file=@Main.java" \
  -F "folderPath=MyProject/src"

curl -X POST http://localhost:8080/api/files/upload \
  -F "file=@README.md" \
  -F "folderPath=MyProject/docs"

# 3. View project structure
curl "http://localhost:8080/api/folders/contents?path=MyProject"

# 4. Download a file
curl -OJ "http://localhost:8080/api/files/download?path=MyProject/src/Main.java"

# 5. Delete entire project
curl -X DELETE "http://localhost:8080/api/folders?path=MyProject"
```

---

## **Part 7: Threading & Performance Demo** ⚡

### **Demo 7.1: Show Thread Pool Configuration**

```bash
# View application.properties
cat src/main/resources/application.properties
```

**Point out:**
```properties
app.async.core-pool-size=5
app.async.max-pool-size=10
app.async.queue-capacity=100
```

**✅ Explain:** "We have 2 thread pools:
1. **File Processing Pool**: 5-10 threads for uploads
2. **Metadata Extraction Pool**: 3-6 threads for processing file info"

---

### **Demo 7.2: Stress Test (Show Concurrent Uploads)**

```bash
# Upload 10 files simultaneously
for i in {1..10}; do
  echo "File $i content" > "concurrent-$i.txt"
  curl -X POST http://localhost:8080/api/async/files/upload \
    -F "file=@concurrent-$i.txt" \
    -F "folderPath=StressTest" &
done

wait
echo "All uploads initiated!"
```

**✅ Show Application Logs:** Multiple threads processing concurrently!

---

## **Part 8: OOP Concepts Demonstration** 🎓

### **Demo 8.1: Show Inheritance**

**Open and explain:**
```bash
# Show base class
cat src/main/java/com/example/jp/model/FileItem.java

# Show specific implementations
cat src/main/java/com/example/jp/model/DocumentFile.java
cat src/main/java/com/example/jp/model/ImageFile.java
```

**✅ Explain:** "FileItem is **abstract base class**, DocumentFile/ImageFile are **concrete subclasses** demonstrating **inheritance**!"

---

### **Demo 8.2: Show Polymorphism**

```bash
# Upload different file types
curl -X POST http://localhost:8080/api/files/upload \
  -F "file=@document.pdf" \
  -F "folderPath=Demo"

curl -X POST http://localhost:8080/api/files/upload \
  -F "file=@image.jpg" \
  -F "folderPath=Demo"
```

**✅ Explain:** "Same `uploadFile()` method handles all types! FileItemFactory creates correct subclass at runtime - that's **polymorphism**!"

---

### **Demo 8.3: Show Factory Pattern**

```bash
cat src/main/java/com/example/jp/model/FileItemFactory.java
```

**✅ Explain:** "Factory Pattern creates appropriate object (DocumentFile, ImageFile, etc.) based on file extension!"

---

## **Part 9: Visual Demo** 👀

### **Demo 9.1: Real-time File System Monitoring**

**In separate terminal:**
```bash
watch -n 1 'tree storage/ && echo "" && ls -lh storage/Documents/'
```

**Then perform operations and show live updates!**

---

### **Demo 9.2: Browser Demo (Optional)**

Create simple HTML:
```html
<!-- demo.html -->
<!DOCTYPE html>
<html>
<head>
    <title>File Upload Demo</title>
</head>
<body>
    <h1>Upload File</h1>
    <form id="uploadForm">
        <input type="file" id="fileInput" multiple>
        <input type="text" id="folderPath" placeholder="Folder Path">
        <button type="submit">Upload</button>
    </form>
    <div id="result"></div>
    
    <script>
        document.getElementById('uploadForm').onsubmit = async (e) => {
            e.preventDefault();
            const formData = new FormData();
            formData.append('file', document.getElementById('fileInput').files[0]);
            formData.append('folderPath', document.getElementById('folderPath').value);
            
            const response = await fetch('http://localhost:8080/api/files/upload', {
                method: 'POST',
                body: formData
            });
            
            document.getElementById('result').innerText = JSON.stringify(await response.json(), null, 2);
        };
    </script>
</body>
</html>
```

---

## **📊 Summary Points for Viva**

### **Key Features Demonstrated:**

1. ✅ **16+ API Endpoints** - Full CRUD operations
2. ✅ **28 Classes** - Well above requirement
3. ✅ **OOP Principles:**
   - Inheritance (FileItem hierarchy)
   - Polymorphism (FileItemFactory)
   - Encapsulation (Services, DTOs)
   - Abstraction (Abstract FileItem class)

4. ✅ **Design Patterns:**
   - Factory Pattern
   - Service Layer Pattern
   - DTO Pattern
   - Repository Pattern (file-based)

5. ✅ **Threading & Concurrency:**
   - Async file uploads
   - Thread pool executors
   - Parallel batch processing
   - CompletableFuture usage

6. ✅ **Real File System:**
   - Actual folders/files on disk
   - No database dependency
   - Visual file management

---

## **🎤 Viva Questions & Answers**

### **Q: Why use file system instead of database?**
**A:** "Simpler deployment, visual verification, no database setup needed, better for file-heavy operations, easier backup (just copy folder)"

### **Q: How does threading improve performance?**
**A:** "Async uploads don't block API, multiple files upload simultaneously using thread pool, better resource utilization, improved user experience"

### **Q: Explain the inheritance hierarchy**
**A:** "FileItem is abstract base with common properties. DocumentFile, ImageFile, etc. extend it with specific properties. Factory creates correct subclass based on file extension"

### **Q: How many threads are used?**
**A:** "2 thread pools: FileProcessingExecutor (5-10 threads) for uploads, MetadataExtractionExecutor (3-6 threads) for processing. Configurable in application.properties"

### **Q: What if two users upload same file name?**
**A:** "FileStorageService checks if file exists, automatically renames to 'filename (1).ext', 'filename (2).ext', etc."

---

## **💡 Pro Tips for Demo**

1. **Prepare files beforehand** - Have sample PDFs, images, text files ready
2. **Keep logs visible** - Show thread activity in real-time
3. **Use Postman** - Can be easier than curl for demo
4. **Have backup** - Keep successful API responses saved
5. **Practice timing** - Demo should be 10-15 minutes
6. **Explain while showing** - Don't just run commands, explain what's happening

---

**Good luck with your demo! 🚀**