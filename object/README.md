# deepfake detection in object images

This contains images, plots and relevant code for deepfake detection in general images (objects).

- `chunk_objects.py` : Code to chunk objects into various partitions, then compute hashes using Imagehash and write to csv.
- `chunk_objects.scala` : Code to the same as above, but using Scala hash programs.
- `chunk_plots.ipynb` : Code used to create the visualization plots for each image, as well as for the averages.

<p align="center">
  <img src="https://github.com/user-attachments/assets/0a6388f2-f621-4cbe-96c4-f43048eb4a2f" alt="chunk_sample">
</p>

## Images

This directory contains (image, fake) pairs for 8 object images. The naming is self explanatory.

An example of (image, fake) pair is:

`object_2.jpg` and `object_2_fake.jpg`

## CSV

There are 2 files, one for either program. They can be distinguished by the prefix of the filename, `py` for python, and `scl` for scala.

Each csv is summarized by the following:
- columns : Each column contains the results for each image. Thus, there are 8 columns corresponding to 8 images.
- rows : Each row denotes the outputs for a chunking level. Row `i` (starting from 1) for Column `image` denotes the hamming distance for `image` when subjected to chunking level `i`. There are 6 rows corresponding to 6 chunking levels ranging from 1 to 6. Chunking level 1 signifies virtually no chunking and using the entire image.

## Plots

There are 9 plots visualizing the results. All the plots have been made using csv files from the `csv` directory. The naming convention is self-explanatory.

- 8 individual plots (1 plot per (image, fake) pair showing chunking effectiveness for a pair)
- 1 average plot (average chunking effectives for all 8 object images)

An example of an (image, fake) pair has been mentioned above.
