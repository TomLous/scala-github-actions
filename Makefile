.DEFAULT_GOAL := test

ROOT_DIR:=$(strip $(shell dirname $(realpath $(lastword $(MAKEFILE_LIST)))))

lint:
	@sbt scalastyle

test:
	@sbt -DcacheToDisk=1 coverage test coverageReport coverageAggregate

package:
	@sbt assembly
	@echo "output/assembly.jar"

version:
	@sbt --error 'set showSuccess := false' showVersion

set-github-config:
	git config --global user.name "$(GITHUB_ACTOR)"
	git config --global user.email "$(GITHUB_ACTOR)@users.noreply.github.com"

bump-snapshot:
	sbt bumpSnapshot

git-push:
	git push
	git push --tags

bump-snapshot-and-push: set-github-config bump-snapshot git-push version

deploy-to-dev-dummy:
	@echo "DEV DEPLOY"

deploy-to-dev: deploy-to-dev-dummy version
