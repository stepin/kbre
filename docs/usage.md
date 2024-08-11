# Usage

## Create new project

You can do it in interactive way using:

```shell
mkdir my-app
cd my-app

kbre init
```

or you can create yaml file manually:
```shell
mkdir my-app
cd my-app

cat > kbre.yaml << \EOF
group: name.stepin
artifact: myapp
name: My app
description: Some description
preset: spring
type: root
extensions:
  - graphql
  - postgres
  - flyway
  - jooq
  - dokka
  - jib
  - local-dev
  - systemd-deployment
variables:
  REPO: http://localhost:3000/stepin/kotlin-bootstrap-app/src/branch/main/src/main/kotlin
  SONAR_HOST_URL: http://localhost:9000
  SONAR_PROJECT_KEY: kotlin-bootstrap-app
  SONAR_PROJECT_NAME: kotlin-bootstrap-app
  SONAR_TOKEN: sqp_821b1d3209761625bdd29259674237d429bce626
EOF

kbre new
```

## Update

It should be safe to run following command:
```shell
cd my-app

kbre update
```

Time to time it's also good to run:
```shell
cd my-app

kbre new
```

It will update also files that most probably have local changes but sometimes it also make sense.
