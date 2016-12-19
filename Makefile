TAG=$(shell ./get_tag.sh)
.PHONY: test

release:
	@read -r -p "Did you bump the version number on project.clj? [y/N] " CONTINUE; \
	if [ -z "$$CONTINUE" ] || ([ $$CONTINUE != "y" ] && [ $$CONTINUE != "Y" ]); then \
			echo "Exiting..." ; exit 1 ; \
	fi 
	@echo "Validating syntax..."
	lein check
	@echo "Done. Starting release for version $(TAG)..."
	git tag $(TAG) && git push origin $(TAG) &&	lein deploy clojars

