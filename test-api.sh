#!/bin/bash

# Test API Script - Quick test of all endpoints

BASE_URL="http://localhost:8080"

echo "🧪 Testing Google Drive-like Backend API"
echo "=========================================="
echo ""

# Test 1: Create a root folder
echo "1️⃣  Creating root folder 'Documents'..."
FOLDER_RESPONSE=$(curl -s -X POST "$BASE_URL/api/folders" \
  -H "Content-Type: application/json" \
  -d '{"name": "Documents", "parentId": null}')
FOLDER_ID=$(echo $FOLDER_RESPONSE | grep -o '"id":[0-9]*' | grep -o '[0-9]*' | head -1)
echo "   Response: $FOLDER_RESPONSE"
echo "   Folder ID: $FOLDER_ID"
echo ""

# Test 2: Get root contents
echo "2️⃣  Getting root folder contents..."
curl -s "$BASE_URL/api/folders/root" | python3 -m json.tool 2>/dev/null || curl -s "$BASE_URL/api/folders/root"
echo ""
echo ""

# Test 3: Create a subfolder
echo "3️⃣  Creating subfolder 'Photos' under Documents..."
SUBFOLDER_RESPONSE=$(curl -s -X POST "$BASE_URL/api/folders" \
  -H "Content-Type: application/json" \
  -d "{\"name\": \"Photos\", \"parentId\": $FOLDER_ID}")
SUBFOLDER_ID=$(echo $SUBFOLDER_RESPONSE | grep -o '"id":[0-9]*' | grep -o '[0-9]*' | head -1)
echo "   Response: $SUBFOLDER_RESPONSE"
echo ""

# Test 4: Create a test file
echo "4️⃣  Creating test file..."
echo "Hello, this is a test file!" > /tmp/test-file.txt
FILE_RESPONSE=$(curl -s -X POST "$BASE_URL/api/files/upload" \
  -F "file=@/tmp/test-file.txt" \
  -F "folderId=$FOLDER_ID")
FILE_ID=$(echo $FILE_RESPONSE | grep -o '"id":[0-9]*' | grep -o '[0-9]*' | head -1)
echo "   Response: $FILE_RESPONSE"
echo "   File ID: $FILE_ID"
echo ""

# Test 5: Get folder contents with file
echo "5️⃣  Getting folder contents (should show subfolder and file)..."
curl -s "$BASE_URL/api/folders/$FOLDER_ID/contents" | python3 -m json.tool 2>/dev/null || curl -s "$BASE_URL/api/folders/$FOLDER_ID/contents"
echo ""
echo ""

echo "✅ All tests completed!"
echo ""
echo "📝 Summary:"
echo "   - Created folder ID: $FOLDER_ID"
echo "   - Created subfolder ID: $SUBFOLDER_ID"
echo "   - Uploaded file ID: $FILE_ID"
echo ""
echo "🌐 You can also access H2 Console at: http://localhost:8080/h2-console"
echo "   JDBC URL: jdbc:h2:file:./data/gdrive_db"
echo "   Username: sa"
echo "   Password: (leave empty)"

