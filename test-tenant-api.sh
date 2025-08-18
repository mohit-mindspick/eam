#!/bin/bash

# EAM Tenant API Test Script
# This script tests the Tenant API functionality

BASE_URL="http://localhost:8080"
API_BASE="$BASE_URL/api"

echo "=========================================="
echo "EAM Tenant API Test Script"
echo "=========================================="
echo "Base URL: $BASE_URL"
echo ""

# Function to print colored output
print_success() {
    echo -e "\033[32m✅ $1\033[0m"
}

print_error() {
    echo -e "\033[31m❌ $1\033[0m"
}

print_info() {
    echo -e "\033[34mℹ️  $1\033[0m"
}

print_header() {
    echo -e "\033[35m📋 $1\033[0m"
}

# Check if the application is running
print_header "Checking if application is running..."
if curl -s "$BASE_URL/actuator/health" > /dev/null; then
    print_success "Application is running"
else
    print_error "Application is not running. Please start the EAM application first."
    exit 1
fi

echo ""

# Test 1: Create Industries
print_header "Test 1: Creating Industries"
echo "Creating Technology industry..."
TECH_RESPONSE=$(curl -s -X POST "$API_BASE/industries" \
    -H "Content-Type: application/json" \
    -d '{"industryCode": "TECH", "name": "Technology"}')

if echo "$TECH_RESPONSE" | grep -q "Technology"; then
    print_success "Technology industry created successfully"
    echo "Response: $TECH_RESPONSE"
else
    print_error "Failed to create Technology industry"
    echo "Response: $TECH_RESPONSE"
fi

echo "Creating Manufacturing industry..."
MFG_RESPONSE=$(curl -s -X POST "$API_BASE/industries" \
    -H "Content-Type: application/json" \
    -d '{"industryCode": "MFG", "name": "Manufacturing"}')

if echo "$MFG_RESPONSE" | grep -q "Manufacturing"; then
    print_success "Manufacturing industry created successfully"
    echo "Response: $MFG_RESPONSE"
else
    print_error "Failed to create Manufacturing industry"
    echo "Response: $MFG_RESPONSE"
fi

echo "Creating Healthcare industry..."
HLTH_RESPONSE=$(curl -s -X POST "$API_BASE/industries" \
    -H "Content-Type: application/json" \
    -d '{"industryCode": "HLTH", "name": "Healthcare"}')

if echo "$HLTH_RESPONSE" | grep -q "Healthcare"; then
    print_success "Healthcare industry created successfully"
    echo "Response: $HLTH_RESPONSE"
else
    print_error "Failed to create Healthcare industry"
    echo "Response: $HLTH_RESPONSE"
fi

echo ""

# Test 2: Get All Industries
print_header "Test 2: Retrieving All Industries"
INDUSTRIES_RESPONSE=$(curl -s -X GET "$API_BASE/industries")
print_info "All Industries:"
echo "$INDUSTRIES_RESPONSE" | jq '.' 2>/dev/null || echo "$INDUSTRIES_RESPONSE"

echo ""

# Test 3: Create Subscriptions
print_header "Test 3: Creating Subscriptions"
echo "Creating Basic subscription..."
BASIC_SUB_RESPONSE=$(curl -s -X POST "$API_BASE/subscriptions" \
    -H "Content-Type: application/json" \
    -d '{"subscriptionCode": "BASIC", "name": "Basic Plan", "description": "Basic subscription plan"}')

if echo "$BASIC_SUB_RESPONSE" | grep -q "Basic Plan"; then
    print_success "Basic subscription created successfully"
    echo "Response: $BASIC_SUB_RESPONSE"
else
    print_error "Failed to create Basic subscription"
    echo "Response: $BASIC_SUB_RESPONSE"
fi

echo "Creating Premium subscription..."
PREMIUM_SUB_RESPONSE=$(curl -s -X POST "$API_BASE/subscriptions" \
    -H "Content-Type: application/json" \
    -d '{"subscriptionCode": "PREMIUM", "name": "Premium Plan", "description": "Premium subscription plan with advanced features"}')

if echo "$PREMIUM_SUB_RESPONSE" | grep -q "Premium Plan"; then
    print_success "Premium subscription created successfully"
    echo "Response: $PREMIUM_SUB_RESPONSE"
else
    print_error "Failed to create Premium subscription"
    echo "Response: $PREMIUM_SUB_RESPONSE"
fi

echo ""

# Test 4: Get All Subscriptions
print_header "Test 4: Retrieving All Subscriptions"
SUBSCRIPTIONS_RESPONSE=$(curl -s -X GET "$API_BASE/subscriptions")
print_info "All Subscriptions:"
echo "$SUBSCRIPTIONS_RESPONSE" | jq '.' 2>/dev/null || echo "$SUBSCRIPTIONS_RESPONSE"

echo ""

# Test 5: Create Tenants
print_header "Test 5: Creating Tenants"
echo "Creating Acme Corporation tenant..."
ACME_RESPONSE=$(curl -s -X POST "$API_BASE/tenants" \
    -H "Content-Type: application/json" \
    -d '{
        "tenantName": "Acme Corporation",
        "tenantCode": "ACME",
        "adminEmail": "admin@acme.com",
        "industryId": 1,
        "subscriptionId": 1,
        "country": "USA",
        "licenses": 100,
        "adminPhoneNumber": "+1-555-0123",
        "featuresEnabled": ["USER_MANAGEMENT", "REPORTING"]
    }')

if echo "$ACME_RESPONSE" | grep -q "Acme Corporation"; then
    print_success "Acme Corporation tenant created successfully"
    echo "Response: $ACME_RESPONSE"
else
    print_error "Failed to create Acme Corporation tenant"
    echo "Response: $ACME_RESPONSE"
fi

echo "Creating TechCorp tenant..."
TECHCORP_RESPONSE=$(curl -s -X POST "$API_BASE/tenants" \
    -H "Content-Type: application/json" \
    -d '{
        "tenantName": "TechCorp Solutions",
        "tenantCode": "TECH",
        "adminEmail": "admin@techcorp.com",
        "industryId": 1,
        "subscriptionId": 2,
        "country": "Canada",
        "licenses": 250,
        "adminPhoneNumber": "+1-555-0456",
        "featuresEnabled": ["USER_MANAGEMENT", "REPORTING", "ADVANCED_ANALYTICS"]
    }')

if echo "$TECHCORP_RESPONSE" | grep -q "TechCorp Solutions"; then
    print_success "TechCorp Solutions tenant created successfully"
    echo "Response: $TECHCORP_RESPONSE"
else
    print_error "Failed to create TechCorp Solutions tenant"
    echo "Response: $TECHCORP_RESPONSE"
fi

echo "Creating HealthCare Inc tenant..."
HEALTH_RESPONSE=$(curl -s -X POST "$API_BASE/tenants" \
    -H "Content-Type: application/json" \
    -d '{
        "tenantName": "HealthCare Inc",
        "tenantCode": "HEALTH",
        "adminEmail": "admin@healthcare.com",
        "industryId": 3,
        "subscriptionId": 2,
        "country": "UK",
        "licenses": 75,
        "adminPhoneNumber": "+44-20-7123-4567",
        "featuresEnabled": ["USER_MANAGEMENT", "COMPLIANCE"]
    }')

if echo "$HEALTH_RESPONSE" | grep -q "HealthCare Inc"; then
    print_success "HealthCare Inc tenant created successfully"
    echo "Response: $HEALTH_RESPONSE"
else
    print_error "Failed to create HealthCare Inc tenant"
    echo "Response: $HEALTH_RESPONSE"
fi

echo ""

# Test 6: Get All Tenants
print_header "Test 6: Retrieving All Tenants"
TENANTS_RESPONSE=$(curl -s -X GET "$API_BASE/tenants")
print_info "All Tenants:"
echo "$TENANTS_RESPONSE" | jq '.' 2>/dev/null || echo "$TENANTS_RESPONSE"

echo ""

# Test 7: Get Specific Tenant
print_header "Test 7: Retrieving Specific Tenant (ID: 1)"
TENANT_1_RESPONSE=$(curl -s -X GET "$API_BASE/tenants/1")
print_info "Tenant with ID 1:"
echo "$TENANT_1_RESPONSE" | jq '.' 2>/dev/null || echo "$TENANT_1_RESPONSE"

echo ""

# Test 8: Update Tenant
print_header "Test 8: Updating Tenant (ID: 1)"
UPDATE_RESPONSE=$(curl -s -X PUT "$API_BASE/tenants/1" \
    -H "Content-Type: application/json" \
    -d '{
        "tenantName": "Acme Corporation Updated",
        "tenantCode": "ACME",
        "adminEmail": "admin.updated@acme.com",
        "industryId": 1,
        "subscriptionId": 1,
        "country": "USA",
        "licenses": 150,
        "adminPhoneNumber": "+1-555-0123",
        "featuresEnabled": ["USER_MANAGEMENT", "REPORTING", "ADVANCED_ANALYTICS"]
    }')

if echo "$UPDATE_RESPONSE" | grep -q "Acme Corporation Updated"; then
    print_success "Tenant updated successfully"
    echo "Response: $UPDATE_RESPONSE"
else
    print_error "Failed to update tenant"
    echo "Response: $UPDATE_RESPONSE"
fi

echo ""

# Test 9: Get Updated Tenant
print_header "Test 9: Retrieving Updated Tenant (ID: 1)"
UPDATED_TENANT_RESPONSE=$(curl -s -X GET "$API_BASE/tenants/1")
print_info "Updated Tenant with ID 1:"
echo "$UPDATED_TENANT_RESPONSE" | jq '.' 2>/dev/null || echo "$UPDATED_TENANT_RESPONSE"

echo ""

# Test 10: Test User Groups by Tenant
print_header "Test 10: Testing User Groups by Tenant"
USER_GROUPS_RESPONSE=$(curl -s -X GET "$API_BASE/user-groups/tenant/1")
print_info "User Groups for Tenant ID 1:"
echo "$USER_GROUPS_RESPONSE" | jq '.' 2>/dev/null || echo "$USER_GROUPS_RESPONSE"

echo ""

# Test 11: Test Locations by Tenant
print_header "Test 11: Testing Locations by Tenant"
LOCATIONS_RESPONSE=$(curl -s -X GET "$API_BASE/locations")
print_info "All Locations:"
echo "$LOCATIONS_RESPONSE" | jq '.' 2>/dev/null || echo "$LOCATIONS_RESPONSE"

echo ""

# Test 12: Health Check
print_header "Test 12: Application Health Check"
HEALTH_CHECK_RESPONSE=$(curl -s -X GET "$BASE_URL/actuator/health")
print_info "Application Health:"
echo "$HEALTH_CHECK_RESPONSE" | jq '.' 2>/dev/null || echo "$HEALTH_CHECK_RESPONSE"

echo ""

# Summary
print_header "Test Summary"
print_success "Tenant API tests completed!"
print_info "Endpoints tested:"
echo "  - POST /api/industries"
echo "  - GET /api/industries"
echo "  - POST /api/subscriptions"
echo "  - GET /api/subscriptions"
echo "  - POST /api/tenants"
echo "  - GET /api/tenants"
echo "  - GET /api/tenants/{id}"
echo "  - PUT /api/tenants/{id}"
echo "  - GET /api/user-groups/tenant/{tenantId}"
echo "  - GET /api/locations"
echo "  - GET /actuator/health"

echo ""
print_info "To run this script again, use: ./test-tenant-api.sh"
print_info "Make sure the EAM application is running on http://localhost:8080"
echo "=========================================="
