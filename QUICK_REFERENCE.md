# 🎯 Quick Reference Guide - Google Drive Backend

## ✅ Application Status: RUNNING & WORKING!

Base URL: `http://localhost:8080`

---

## 📡 API Endpoints Cheat Sheet

### 1. Create a Root Folder
```bash
curl -X POST http://localhost:8080/api/folders \
  -H "Content-Type: application/json" \
  -d '{"name": "Documents", "parentId": null}'
```
**Response:**
```json
{"id":3,"name":"Documents","parentId":null,"createdAt":"...","updatedAt":"..."}
```

---

### 2. Create a Subfolder
```bash
curl -X POST http://localhost:8080/api/folders \
  -H "Content-Type: application/json" \
  -d '{"name": "Photos", "parentId": 3}'
```
Replace `3` with your parent folder ID.

---

### 3. Upload a File
```bash
# First, make sure the file exists
curl -X POST http://localhost:8080/api/files/upload \
  -F "file=@/path/to/your/file.pdf" \
  -F "folderId=3"
```

**Example with test file:**
```bash
echo "Test content" > myfile.txt
curl -X POST http://localhost:8080/api/files/upload \
  -F "file=@myfile.txt" \
  -F "folderId=3"
```

**Response:**
```json
{"id":2,"name":"myfile.txt","size":49,"mimeType":"text/plain","folderId":3,"createdAt":"...","updatedAt":"..."}
```

---

### 4. Get Root Folder Contents
```bash
curl http://localhost:8080/api/folders/root
```

**Pretty print with JSON:**
```bash
curl -s http://localhost:8080/api/folders/root | python3 -m json.tool
```

---

### 5. Get Folder Contents (with files)
```bash
curl http://localhost:8080/api/folders/3/contents
```
Replace `3` with your folder ID.

**Pretty print:**
```bash
curl -s http://localhost:8080/api/folders/3/contents | python3 -m json.tool
```

---

### 6. Download a File
```bash
curl -O -J http://localhost:8080/api/files/2/download
```
Replace `2` with your file ID.

**Or save with custom name:**
```bash
curl http://localhost:8080/api/files/2/download -o my-downloaded-file.txt
```

---

### 7. Delete a File
```bash
curl -X DELETE http://localhost:8080/api/files/2
```
Replace `2` with your file ID.

---

### 8. Delete a Folder
```bash
curl -X DELETE http://localhost:8080/api/folders/3
```
Replace `3` with your folder ID.

⚠️ **Warning:** This will delete all files and subfolders inside!

---

## 🗄️ Database Access

**H2 Console:** http://localhost:8080/h2-console

**Login Credentials:**
- **JDBC URL:** `jdbc:h2:file:./data/gdrive_db`
- **Username:** `sa`
- **Password:** *(leave empty)*

**Useful SQL Queries:**
```sql
-- View all folders
SELECT * FROM folders;

-- View all files
SELECT * FROM file_items;

-- View folder hierarchy
SELECT f.id, f.name, f.parent_id, p.name as parent_name 
FROM folders f 
LEFT JOIN folders p ON f.parent_id = p.id;

-- Count files per folder
SELECT f.name, COUNT(fi.id) as file_count 
FROM folders f 
LEFT JOIN file_items fi ON f.id = fi.folder_id 
GROUP BY f.name;
```

---

## 🧪 Complete Test Flow

```bash
# 1. Create root folder
curl -X POST http://localhost:8080/api/folders \
  -H "Content-Type: application/json" \
  -d '{"name": "My Drive", "parentId": null}'
# Note the returned ID (e.g., 4)

# 2. Create subfolder
curl -X POST http://localhost:8080/api/folders \
  -H "Content-Type: application/json" \
  -d '{"name": "Work Documents", "parentId": 4}'
# Note the returned ID (e.g., 5)

# 3. Create a test file
echo "Important work document" > work-doc.txt

# 4. Upload file to subfolder
curl -X POST http://localhost:8080/api/files/upload \
  -F "file=@work-doc.txt" \
  -F "folderId=5"
# Note the returned file ID (e.g., 3)

# 5. List root contents
curl -s http://localhost:8080/api/folders/root | python3 -m json.tool

# 6. List subfolder contents
curl -s http://localhost:8080/api/folders/5/contents | python3 -m json.tool

# 7. Download the file
curl -o downloaded-file.txt http://localhost:8080/api/files/3/download

# 8. Verify downloaded file
cat downloaded-file.txt
```

---

## 🔧 Common Issues & Solutions

### Issue: File not found error
**Error:** `curl: (26) Failed to open/read local data`

**Solution:** Make sure the file exists before uploading
```bash
# Check file exists
ls -la myfile.pdf

# Create test file if needed
echo "test content" > myfile.txt
```

---

### Issue: Port 8080 already in use
**Solution:**
```bash
# Kill process on port 8080
lsof -ti:8080 | xargs kill -9

# Restart application
./start.sh
```

---

### Issue: Application not starting
**Solution:**
```bash
# Clean rebuild
./mvnw clean install -DskipTests

# Start fresh
./start.sh
```

---

## 📁 File Locations

- **Uploaded files:** `uploads/` directory
- **Database:** `data/gdrive_db.mv.db`
- **Logs:** Console output

---

## 💡 Tips & Tricks

### 1. Upload multiple files
```bash
for file in *.txt; do
  curl -X POST http://localhost:8080/api/files/upload \
    -F "file=@$file" \
    -F "folderId=3"
done
```

### 2. Create multiple folders
```bash
for name in "Documents" "Photos" "Videos" "Music"; do
  curl -X POST http://localhost:8080/api/folders \
    -H "Content-Type: application/json" \
    -d "{\"name\": \"$name\", \"parentId\": null}"
done
```

### 3. Pretty print all API responses
Add this to your `.zshrc` or `.bashrc`:
```bash
alias api-get='curl -s | python3 -m json.tool'
```

Then use:
```bash
curl -s http://localhost:8080/api/folders/root | api-get
```

---

## 🎯 Response Codes

- **200 OK** - Success
- **201 Created** - Resource created successfully
- **204 No Content** - Deleted successfully
- **400 Bad Request** - Invalid data
- **404 Not Found** - Resource not found
- **500 Internal Server Error** - Server error

---

## 📊 Test Data

Want to populate with test data? Run:

```bash
# Create folder structure
curl -X POST http://localhost:8080/api/folders \
  -H "Content-Type: application/json" \
  -d '{"name": "Personal", "parentId": null}'

curl -X POST http://localhost:8080/api/folders \
  -H "Content-Type: application/json" \
  -d '{"name": "Work", "parentId": null}'

# Upload some test files
echo "Meeting notes for Q4 2025" > notes.txt
curl -X POST http://localhost:8080/api/files/upload \
  -F "file=@notes.txt" \
  -F "folderId=1"

echo "Project proposal draft" > proposal.txt
curl -X POST http://localhost:8080/api/files/upload \
  -F "file=@proposal.txt" \
  -F "folderId=1"
```

---

**Application is running and fully functional! 🚀**

Access H2 Console: http://localhost:8080/h2-console

