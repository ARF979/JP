#!/bin/bash

# Complete Demo Script - Google Drive Backend
# This script demonstrates all API features

BASE_URL="http://localhost:8080"
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${BLUE}╔════════════════════════════════════════════════════════╗${NC}"
echo -e "${BLUE}║   Google Drive-like Backend - Complete Demo           ║${NC}"
echo -e "${BLUE}╔════════════════════════════════════════════════════════╗${NC}"
echo ""

# Check if server is running
echo -e "${YELLOW}🔍 Checking if server is running...${NC}"
if ! curl -s "$BASE_URL/api/folders/root" > /dev/null; then
    echo -e "${RED}❌ Server is not running! Please start it with: ./start.sh${NC}"
    exit 1
fi
echo -e "${GREEN}✓ Server is running!${NC}"
echo ""

# 1. Create Root Folders
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${YELLOW}📁 Step 1: Creating root folders...${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"

PERSONAL_RESPONSE=$(curl -s -X POST "$BASE_URL/api/folders" \
  -H "Content-Type: application/json" \
  -d '{"name": "Personal", "parentId": null}')
PERSONAL_ID=$(echo $PERSONAL_RESPONSE | grep -o '"id":[0-9]*' | grep -o '[0-9]*' | head -1)
echo -e "${GREEN}✓ Created 'Personal' folder (ID: $PERSONAL_ID)${NC}"

WORK_RESPONSE=$(curl -s -X POST "$BASE_URL/api/folders" \
  -H "Content-Type: application/json" \
  -d '{"name": "Work", "parentId": null}')
WORK_ID=$(echo $WORK_RESPONSE | grep -o '"id":[0-9]*' | grep -o '[0-9]*' | head -1)
echo -e "${GREEN}✓ Created 'Work' folder (ID: $WORK_ID)${NC}"
echo ""

# 2. Create Subfolders
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${YELLOW}📂 Step 2: Creating subfolders...${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"

PHOTOS_RESPONSE=$(curl -s -X POST "$BASE_URL/api/folders" \
  -H "Content-Type: application/json" \
  -d "{\"name\": \"Photos\", \"parentId\": $PERSONAL_ID}")
PHOTOS_ID=$(echo $PHOTOS_RESPONSE | grep -o '"id":[0-9]*' | grep -o '[0-9]*' | head -1)
echo -e "${GREEN}✓ Created 'Personal/Photos' subfolder (ID: $PHOTOS_ID)${NC}"

PROJECTS_RESPONSE=$(curl -s -X POST "$BASE_URL/api/folders" \
  -H "Content-Type: application/json" \
  -d "{\"name\": \"Projects\", \"parentId\": $WORK_ID}")
PROJECTS_ID=$(echo $PROJECTS_RESPONSE | grep -o '"id":[0-9]*' | grep -o '[0-9]*' | head -1)
echo -e "${GREEN}✓ Created 'Work/Projects' subfolder (ID: $PROJECTS_ID)${NC}"
echo ""

# 3. Create Test Files
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${YELLOW}📝 Step 3: Creating test files...${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"

# Create test files
echo "This is a meeting notes document for Q4 2025" > /tmp/meeting-notes.txt
echo "Project proposal for the new backend system" > /tmp/proposal.txt
echo "Personal diary entry for November 2025" > /tmp/diary.txt

echo -e "${GREEN}✓ Created 3 test files${NC}"
echo ""

# 4. Upload Files
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${YELLOW}⬆️  Step 4: Uploading files...${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"

FILE1_RESPONSE=$(curl -s -X POST "$BASE_URL/api/files/upload" \
  -F "file=@/tmp/meeting-notes.txt" \
  -F "folderId=$WORK_ID")
FILE1_ID=$(echo $FILE1_RESPONSE | grep -o '"id":[0-9]*' | grep -o '[0-9]*' | head -1)
echo -e "${GREEN}✓ Uploaded 'meeting-notes.txt' to Work folder (File ID: $FILE1_ID)${NC}"

FILE2_RESPONSE=$(curl -s -X POST "$BASE_URL/api/files/upload" \
  -F "file=@/tmp/proposal.txt" \
  -F "folderId=$PROJECTS_ID")
FILE2_ID=$(echo $FILE2_RESPONSE | grep -o '"id":[0-9]*' | grep -o '[0-9]*' | head -1)
echo -e "${GREEN}✓ Uploaded 'proposal.txt' to Work/Projects folder (File ID: $FILE2_ID)${NC}"

FILE3_RESPONSE=$(curl -s -X POST "$BASE_URL/api/files/upload" \
  -F "file=@/tmp/diary.txt" \
  -F "folderId=$PERSONAL_ID")
FILE3_ID=$(echo $FILE3_RESPONSE | grep -o '"id":[0-9]*' | grep -o '[0-9]*' | head -1)
echo -e "${GREEN}✓ Uploaded 'diary.txt' to Personal folder (File ID: $FILE3_ID)${NC}"
echo ""

# 5. List Root Contents
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${YELLOW}📋 Step 5: Listing root folder contents...${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
curl -s "$BASE_URL/api/folders/root" | python3 -m json.tool 2>/dev/null || curl -s "$BASE_URL/api/folders/root"
echo ""

# 6. List Folder Contents
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${YELLOW}📋 Step 6: Listing 'Work' folder contents...${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
curl -s "$BASE_URL/api/folders/$WORK_ID/contents" | python3 -m json.tool 2>/dev/null || curl -s "$BASE_URL/api/folders/$WORK_ID/contents"
echo ""

# 7. Download File
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${YELLOW}⬇️  Step 7: Downloading a file...${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
curl -s "$BASE_URL/api/files/$FILE1_ID/download" -o /tmp/downloaded-meeting-notes.txt
echo -e "${GREEN}✓ Downloaded file to /tmp/downloaded-meeting-notes.txt${NC}"
echo -e "${YELLOW}Content:${NC}"
cat /tmp/downloaded-meeting-notes.txt
echo ""
echo ""

# Summary
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${GREEN}✅ Demo Complete! Summary:${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo ""
echo -e "${YELLOW}Created Folders:${NC}"
echo "  • Personal (ID: $PERSONAL_ID)"
echo "    └── Photos (ID: $PHOTOS_ID)"
echo "  • Work (ID: $WORK_ID)"
echo "    └── Projects (ID: $PROJECTS_ID)"
echo ""
echo -e "${YELLOW}Uploaded Files:${NC}"
echo "  • meeting-notes.txt → Work (File ID: $FILE1_ID)"
echo "  • proposal.txt → Work/Projects (File ID: $FILE2_ID)"
echo "  • diary.txt → Personal (File ID: $FILE3_ID)"
echo ""
echo -e "${YELLOW}💡 Next Steps:${NC}"
echo "  • View H2 Console: ${BLUE}http://localhost:8080/h2-console${NC}"
echo "  • API Documentation: ${BLUE}QUICK_REFERENCE.md${NC}"
echo "  • Delete a folder: ${BLUE}curl -X DELETE $BASE_URL/api/folders/$PHOTOS_ID${NC}"
echo "  • Delete a file: ${BLUE}curl -X DELETE $BASE_URL/api/files/$FILE3_ID${NC}"
echo ""
echo -e "${GREEN}🎉 Your Google Drive-like backend is fully functional!${NC}"
echo ""

