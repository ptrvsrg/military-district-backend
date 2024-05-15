#!/bin/bash

echo "Creating databases..."
echo "$POSTGRESQL_PASSWORD" | createdb -U "$POSTGRESQL_USERNAME" "keycloak"
echo "$POSTGRESQL_PASSWORD" | createdb -U "$POSTGRESQL_USERNAME" "military_district"