# detection in face-swap images

This contains images, plots and relevant code for deepfake detection in face-swap images.

- `chunk_face.py` : Code to chunk faces into various partitions, then compute hashes using Imagehash and write to csv.
- `chunk_face.scala` : Code to the same as above, but using Scala hash programs.
- `chunk_plots.ipynb` : Code used to perform the visualization for each image, as well as for the averages.

<p align="center">
  <img src="https://github.com/user-attachments/assets/feaa5b37-2d68-4770-b12a-18026c6f98bc" alt="chunk_sample">
</p>

## Images

There are 4 basic images of faces, 2 male (example:`m1.jpg`) and 2 female (example:`f1.jpg`).

There are 4 face-swap images that we want to investigate. An image is of the form: `f_swap_1_2`. It signifies that the face of source image `f1` has been added to the target image `f2`.

## CSV

There are 2 files, one for either program. They can be distinguished by the prefix of the filename, `py` for python, and `scl` for scala.

Each csv is summarized by the following:
- columns : Each column contains the results for each image. Thus, there are 4 columns corresponding to 4 face-swap images.
- rows : Each row denotes the outputs for a chunking level. Row `i` (starting from 1) for Column `image` denotes the hamming distance between `image` and it'starget image when subjected to chunking level `i`. There are 6 rows, corresponding to a chunking level ranging from 1 to 6. Chunking level 1 signifies virtually no chunking and using the entire image.

## Plots

There are 5 plots:
- 4 plots, one for each face-swap image. They plot the corresponding columns for that image from the python and scala csv files.
- 1 plot for averaging the results from all 4 face-swap images. This helps us to get some idea about the chunking effectiveness for face-swap images.

