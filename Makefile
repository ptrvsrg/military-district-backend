HOME_DIR = $(shell pwd)
SAMPLE_ENV_FILE = $(HOME_DIR)/sample.env
ENV_FILE = $(HOME_DIR)/.env
MODULES=ms-equipment ms-formation ms-infrastructure ms-military ms-weapon
APOLLO_MODULES=rover router
MAVEN = $(HOME_DIR)/mvnw
DOCKER = docker
DOCKER_COMPOSE = docker compose

define build_module_image
$(DOCKER) build \
-t ptrvsrg/$(1):latest \
-t ptrvsrg/$(1):$(shell $(MAVEN) help:evaluate -Dexpression=project.version -q -DforceStdout) \
$(HOME_DIR)/$(1);
endef

define build_apollo_module_image
$(DOCKER) build \
-t ptrvsrg/apollographql-$(1):latest \
$(HOME_DIR)/$(1);
endef

.PHONY: clean
clean:
	$(MAVEN) clean

.PHONY: build
build:
	$(MAVEN) clean install spring-boot:repackage

.PHONY: build-images
build-images: build
	$(foreach module,$(MODULES),$(call build_module_image,$(module)))
	$(foreach module,$(APOLLO_MODULES),$(call build_apollo_module_image,$(module)))

.PHONY: env
env:
	@cp $(SAMPLE_ENV_FILE) $(ENV_FILE)

.PHONY: deploy
deploy: build
	$(DOCKER_COMPOSE) --env-file $(ENV_FILE) -p military-district up -d

.PHONY: help
help:
	@echo "Available commands:"
	@echo "    make clean"
	@echo "        Clean generated files"
	@echo "    make build"
	@echo "        Build the JAR files"
	@echo "    make build-images"
	@echo "        Build the Docker images"
	@echo "    make env"
	@echo "        Create template .env file"
	@echo "    make deploy"
	@echo "        Deploy to Docker"
	@echo "    make help"
	@echo "        Display this message"

.DEFAULT_GOAL := help