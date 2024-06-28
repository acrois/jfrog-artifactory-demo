#!/bin/bash
sed -i 's/allowNonPostgresql: false/allowNonPostgresql: true/' /var/opt/jfrog/artifactory/etc/system.yaml
exec /entrypoint-artifactory.sh