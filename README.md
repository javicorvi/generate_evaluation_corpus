Evaluation corpus generated from different sources regarding to evaluation de treatment-related finding tagger.

Corpus 1:

H. Gurulingappa, R. Klinger, M. Hofmann-Apitius, J. Fluck, 
An empirical evaluation of resources for the identification of diseases and adverse effects in biomedical literature, in: 
The 2nd Workshop on Building and Evaluating Resources for Biomedical Text Mining (7th edition of the Language Resources and Evaluation Conference), 
2010, pp. 15–22.

Origin: Medline

Type: Literature

Quantity: 400 abstracts,  1428 disease and 813 adverse effect annotations.

https://www.scai.fraunhofer.de/content/dam/scai/de/downloads/bioinformatik/Disease-ae-corpus.iob

Original Entities annotated and mapping for evaluation:
	DISEASES ---> FINDING
	ADVERSE  ---> FINDING

Corpus 2:

Gurulingappa et al., Benchmark Corpus to Support Information Extraction for Adverse Drug Effects, JBI, 2012.
http://www.sciencedirect.com/science/article/pii/S1532046412000615

Origin: PubMed Central documents

Type: Literature

Quantity: 2972 abstracts (4272 sentences)

https://sites.google.com/site/adecorpus/

Original Entities annotated and mapping for evaluation:
	Drug                    
	adverse effect   ---> FINDING
	dosage           ---> DOSE_QUANTITY

Corpus 3: 

Demner-Fushman D, Mork JG, Rogers WJ, Shooshan SE, Rodriguez L, Aronson AR. Finding medication doses in the literature. AMIA Annu Symp Proc. 2018;2018:368–376. Published 2018 Dec 5.

Origin: PubMed Central documents

Type: Literature

Quantity: 694 PubMed Central documents

https://ii.nlm.nih.gov/DataSets/index.shtml

Original Entities annotated and mapping for evaluation:
	Medication
	Dose      ---> DOSE_QUANTITY
	Strength   
	Form
	Route     ---> ROUTE_OF_ADMINISTRATION
	Frequency ---> DOSE_FREQUENCY
	Duration  ---> DOSE_DURATION 
	Reason
	
Corpus 4:

TAC 2017
The Text Analysis Conference (TAC) is a series of evaluation workshops organized to encourage research in 
Natural Language Processing and related applications, by providing a large test collection, common evaluation procedures, 
and a forum for organizations to share their results.

Challenge: Adverse Drug Reaction Extraction from Drug Labels

Origin: TAC 2017

Type: Drug Labels

Quantity: 101 drug labels from the training set.

https://bionlp.nlm.nih.gov/tac2017adversereactions/

Original Entities annotated and mapping for evaluation:
	AdverseReactions  ---> FINDING
	Severity	      ---> SEVERITY
	Factor
	DrugClass
	Negation
	Animal            ---> SPECIES


