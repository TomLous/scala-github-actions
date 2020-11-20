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

bump-release:
	sbt bumpRelease

bump-patch:
	sbt bumpPatch

git-push:
	git push
	git push --tags

bump-snapshot-and-push: set-github-config bump-snapshot git-push version
bump-release-and-push: set-github-config bump-release git-push version
bump-patch-and-push: set-github-config bump-patch git-push version

deploy-to-dev-dummy:
	@echo "DEV DEPLOY"

deploy-to-staging-dummy:
	@echo "STAGING DEPLOY"

deploy-to-prod-dummy:
	@echo "PROD DEPLOY"

deploy-to-dev: deploy-to-dev-dummy version
deploy-to-staging: deploy-to-staging-dummy version
deploy-to-prod: deploy-to-prod-dummy version

check-changes:
	@echo $$(git diff --name-only HEAD HEAD~1 | cat |  grep -v version.sbt || true | wc -l | tr -d ' ')

create-hotfix-branch:
	git fetch
	git checkout -b hotfix $$(git describe --tags --abbrev=0 | grep -E "^v[0-9]+\.[0-9]+\.[0-9]+$$")
