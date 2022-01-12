# PRIMAT: Private Matching Toolbox

<img src="primat_logo.png" width="250">


PRIMAT is an open source (ALv2) toolbox for the definition and execution of PPRL workflows. 
It offers several components for data owners and the central linkage unit that provide state-of-the-art PPRL methods,
including Bloom-filter-based encoding and hardening techniques, LSH-based blocking, metric space filtering,
post-processing and more.


[PRIMAT](https://dl.acm.org/citation.cfm?doid=3352063.3360392) is developed by the [Database Group](https://dbs.uni-leipzig.de/research/projects/pper_big_data) of the University of Leipzig, Germany.

⚠️⚠️⚠️ Attention: This repository contains the first PRIMAT release which was presented at the VLDB 2019. Information on the (demo) showcase applications can be found below. Since then we did an extensive refactoring of the code base to simplify usage and to improve extensibility and maintainability. As of December 2021, new PRIMAT versions are released at https://git.informatik.uni-leipzig.de/dbs/pprl/primat.


## Privacy-preserving Record Linkage

- Task of identifying record in different databases reffering to the same person
- Protection of sensitive personal information
- Applications in medicine & healthcare, national security and marketing analysis

<img src="https://user-images.githubusercontent.com/20927034/118960531-acfb8e00-b963-11eb-894e-ecffafbd8f87.png" width="500">

### Key Challenges

- Gurantee privacy by minimizing disclosure risk
- Scalability to millions of records
- High linkage quality

## PRIMAT

- PPRL tool covering the entire PPRL life-cycle
- Flexible definition and execution of PPRL workflows
- Comparative evaluation of PPRL approaches
- Modules for both data owner and the trusted linkage unit

<img src=https://user-images.githubusercontent.com/20927034/118961272-707c6200-b964-11eb-9ed9-8264e04cc840.png width="500">

### State-of-the-art PPRL Methods

#### Bloom filter encodings & hardening techniques

<img src=https://user-images.githubusercontent.com/20927034/118971359-9c511500-b96f-11eb-8f41-986724c7db92.png width="400">

#### Fast & private blocking/filtering techniques

<img src=https://user-images.githubusercontent.com/20927034/118971495-ca365980-b96f-11eb-88fb-b7478288a2dc.png width="400">

#### Post-processing methods for one-to-one link restriction

<img src=https://user-images.githubusercontent.com/20927034/118971617-f05bf980-b96f-11eb-8bcc-d1d0a4a0114e.png width="400">


### Functional Overview 

|Component/Module | Function/Feature | Status |
|-----------------|------------------|--------|
| Data generator & corruptor | - Data generation<br> - Data corruption | Implemented<br>Planned |
| Data cleaning | - Split/merge/remove attributes<br>- Replace/remove unwanted values<br>- OCR transformation | Implemented<br>Implemented<br>Implemented |
| Encoding | - Bloom filter encoding & hardening<br>- Support of alternative encoding schemes| Implememnted<br>Planned |
| Matching | - Standard & LSH-based blocking, Metric Space filtering<br>- Threshold-based classification<br>- Post-processing<br>- Multi-threaded execution<br>- Distributed matching<br>- Multi-Party support, match cluster management<br>- Incremental Matching | Implemented<br>Implemented<br>Implemented<br>Implemented<br>Integration outstanding<br>In development<br>In development |
| Evaluation | - Measure for assessing quality & scalability<br>- Masked match result visualization | Implemented<br>Integration outstanding |

### Requirements

- Java 11+ 
- Maven
- Ubuntu (recommended)

## Showcase Applications

### Data Owner App

The data owner application consists of components for pre-processing (data cleaning and stardadization) functions and Bloom-filter-based encoding of records containing person-related data.

To run the data owner application run the following command in the primat directory (where the pom is located):

`mvn clean javafx:run -Dprimat.mainClass=dbs.pprl.toolbox.data_owner.gui.DataOwnerApp`


### Linkage Unit App

The linkage unit application provides linkage functionalities, in particular blocking, similarity calculation and classification, post-processing. Furthermore, it consists of evaluation facilities to compare different PPRL workflows in terms of quality (recall, precision, f-measure) and scalability (runtime, reduction ratio).

To run the linkage unit application run the following command in the primat directory (where the .pom-file is located):

`mvn clean javafx:run -Dprimat.mainClass=dbs.pprl.toolbox.lu.gui.LinkageUnitApp`
