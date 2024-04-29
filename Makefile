HOME_DIR = $(shell pwd)
SAMPLE_ENV_FILE = $(HOME_DIR)/sample.env
ENV_FILE = $(HOME_DIR)/.env
MODULES=ms-equipment ms-formation ms-infrastructure ms-military ms-report ms-weapon
APOLLO_MODULES=rover router
MAVEN = $(HOME_DIR)/mvnw
DOCKER = docker
DOCKER_COMPOSE = docker compose

define build_module_image
$(DOCKER) build \
-t $(2)$(1):latest \
-t $(2)$(1):$(shell $(MAVEN) help:evaluate -Dexpression=project.version -q -DforceStdout) \
$(HOME_DIR)/$(1);
endef

define build_apollo_module_image
$(DOCKER) build \
-t $(2)apollographql-$(1):latest \
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
	$(foreach module,$(MODULES),$(call build_module_image,$(module),$(IMAGE_PREFIX)))
	$(foreach module,$(APOLLO_MODULES),$(call build_apollo_module_image,$(module),$(IMAGE_PREFIX)))

.PHONY: env
env:
	@cp $(SAMPLE_ENV_FILE) $(ENV_FILE)

.PHONY: supergraph
supergraph:
	$(DOCKER) run \
	-v ./:/data ptrvsrg/apollographql-rover:latest \
	supergraph compose --config /data/supergraph.yaml --output /data/supergraph.graphqls --elv2-license=accept;
	sudo chown $(shell whoami):$(shell whoami) ./supergraph.graphqls


.PHONY: deploy
deploy:
ifeq ($(shell [ -e ./.env ] && echo 1 || echo 0), 1)
	$(DOCKER_COMPOSE) --env-file $(ENV_FILE) -p military-district up -d
else
	$(DOCKER_COMPOSE) -p military-district up -d
endif

.PHONY: help
help:
	@echo "Available commands:"
	@echo "    make clean"
	@echo "        Clean generated files"
	@echo "    make build"
	@echo "        Build the JAR files"
	@echo "    make build-images IMAGE_PREFIX=<image prefix>"
	@echo "        Build the Docker images"
	@echo "    make env"
	@echo "        Create template .env file"
	@echo "    make supergraph"
	@echo "        Generate supergraph schema"
	@echo "    make deploy"
	@echo "        Deploy to Docker"
	@echo "    make help"
	@echo "        Display this message"

.DEFAULT_GOAL := help