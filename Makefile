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
	cat version.sbt | awk '{print $5}' | tr -d '"'

set-github-config:
	git config --global user.name "$(GITHUB_ACTOR)"
	git config --global user.email "$(GITHUB_ACTOR)@users.noreply.github.com"

bump-snapshot:
	sbt bumpSnapshot

bump-release:
	sbt bumpRelease

git-push:
	git push
	git push --tags

bump-snapshot-and-push: set-github-config bump-snapshot git-push version
bump-release-and-push: set-github-config bump-release git-push version

deploy-to-dev-dummy:
	@echo "DEV DEPLOY"

deploy-to-staging-dummy:
	@echo "STAGING DEPLOY"

deploy-to-dev: deploy-to-dev-dummy version
deploy-to-staging: deploy-to-staging-dummy version
