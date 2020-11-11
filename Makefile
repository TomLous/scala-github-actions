.DEFAULT_GOAL := test

ROOT_DIR:=$(strip $(shell dirname $(realpath $(lastword $(MAKEFILE_LIST)))))

lint:
	@sbt scalastyle

test:
	@sbt -DcacheToDisk=1 coverage test coverageReport coverageAggregate

package:
	@sbt assembly
	@echo "output/assembly.jar"

deploy-to-dev:
	@echo "DEV DEPLOY"
	@sbt --error 'set showSuccess := false' showVersion
