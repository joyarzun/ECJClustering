PR := java -Xmx1024m ec.Evolve -file source.params -p stat.gather-full=true -p gp.tree.print-style=s-expression
CP := mkdir checkpoint$$i; mv ec.*.gz checkpoint$$i
dirlog := log
DL := default
PRLOG := mkdir "$(dirlog)/$(DL)"; mv out.stat "$(dirlog)/$(DL)"; mv log_* "$(dirlog)/$(DL)"; mv checkpoint* "$(dirlog)/$(DL)"; mv bestInd.txt "$(dirlog)/$(DL)"

run: 
	for i in $$(seq 1 1); do $(PR); $(CP); done
	
clean:
	rm -f log_resumen.log
	rm -f log_estadistica.log
	rm -f log_normal.log
	rm -f log_all.log
	rm -f out.stat
	rm -f salida*
	rm -f ec.*.gz
	rm -rf checkpoint*
	rm -f bestInd.txt
	
compile:
	javac -encoding UTF-8 *.java
	
cplog:
	if [ ! -d "$(dirlog)/$(DL)" ]; then $(PRLOG); else echo "EL DIRECTORIO EXISTE"; fi

test: compile
	$(PR)
aaa:
	for i in $$(seq 5); do echo $$i; done
