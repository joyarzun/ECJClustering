PR := java -Xmx2048m ec.Evolve -file source.params -p stat.gather-full=true -p gp.tree.print-style=s-expression
CP := mkdir checkpoint$$i; mv ec.*.gz checkpoint$$i
dirlog := log
DL := default
PRLOG := mkdir "$(dirlog)/$(DL)"; mv out.stat "$(dirlog)/$(DL)"; mv log_* "$(dirlog)/$(DL)"; mv checkpoint* "$(dirlog)/$(DL)"; mv bestInd.txt "$(dirlog)/$(DL)"

run: compile clean
	for i in {1..30}; do $(PR); $(CP); done
	
clean:
	rm -f log_resumen.log
	rm -f log_estadistica.log
	rm -f ec.*.gz
	rm -rf checkpoint
	rm -f bestInd.txt
	
compile:
	javac *.java
	
cplog:
	if [ ! -d "$(dirlog)/$(DL)" ]; then $(PRLOG); else echo "EL DIRECTORIO EXISTE"; fi

test: compile
	$(PR)