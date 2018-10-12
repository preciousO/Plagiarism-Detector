# Plagiarism-Detector
*******************************************************************************
Plagiarism Detector takes in two files as input and tells you the similarity
between the two files in percentage. 
It also takes in a synonyms file where each line is 
considered a synonym group e.g a line containing "run sprint jog" is considered
a synonym group.
Finally, you can also specify what the tuple size should be. By default , the
tuple size is 3.
*******************************************************************************
This Read Me explains how to run the program

1. Using the command line , navigate to the directory where the project is saved

2. Enter `javac PlagiarismDetector.java`

3. Enter `java PlagiarismDetector <synonymsFile> <firstFile> <secondFile> <tupleSize>`
   e.g `java PlagiarismDetector syns.txt file1.txt file2.txt 2`

4. Alternatively you could enter `java PlagiarismDetector syns.txt <firstFile> <secondFile>`
   e.g `java PlagiarismDetector <synonymsFile> file1.txt file2.txt`
   NOTE: The program will select the default tuple size of 3
*******************************************************************************
