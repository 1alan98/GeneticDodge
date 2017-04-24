# GeneticDodge
This is a project I began working on during spring break. It's still a work in progress. GeneticDodge is a fairly simple genetic algorithm
program. It consists of two characters a Dodge, and a missle. The Dodge and missle travel towards each other. The objective
is for the Dodge to jump over the missle. If the Dodge, hits the missle, the Dodge gains one fitness point. The program first
makes 100 Dodges at random, and tests each one. After each generation, the best Dodges (those with the lowest fitness scores)
survive and produce offspring. Currently this occurs relatively quick as there's only a few factors that affect a Dodges 
ability to jump over the missle effectively. 
Currently my main priority is to clean up the code to reduce reduncies and improve readbility, and to 
modify method comments to a PRE, POST format. I plan on improving the physics of the game and adding more factors - such as 
friction and weight. I also plan on increasing the complexity of the Dodge by adding muscular structure and joints, and by 
making the Dodge's decision making strategy much more sophisticated (for this I will also increase the variability in the
missle's behavoirs).
