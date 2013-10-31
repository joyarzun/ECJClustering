PR := java ec.Evolve -file source.params -p stat.gather-full=true -p gp.tree.print-style=s-expression
run: compile clean
	for i in {1..2}; do $(PR); done
	
clean:
	rm -f log_resumen.log
	rm -f log_estadistica.log
	
compile:
	javac *.java