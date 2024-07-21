# noise sensitivity

- The script `add_noise.py` iterates over images and adds noise (uniform and gaussian) to them.
- The code `noising.py` is responsible for iterating over the noised and source images, computing hashes using ImageHash
and storing the hamming distances in a csv file for further manipulations
- The code `noising.scala` does the same as the aforementioned python version, but it makes use of the Scala program. It then
writes hamming distances to a similar csv.
- The code `noise_plots.ipynb` is used to make all the plots for noise sensitivity, including plots for individual images as well as averages.
All the plots have been made using the csv files in the 'csv' directory.

Note : The csv files for gaussian and uniform noise have been separated for simplification.

<p align="center">
  <img src="https://github.com/user-attachments/assets/c858f6d3-4bd4-45e7-bd76-f078bc0b45bd" alt="noise_sample">
</p>

## Images

This directory contains images of male and female faces, as well as objects. Below are some examples to aid understanding:

`f1.jpg` : Face image of female 1, `m2.jpg` : Face image of male 2, `o3.jpg` : Image of object 3

## CSV

Contains 4 csv files. 2 for python and 2 for scala. Each csv contains different hamming distances for all the different images. The columns represent the different images. For example, there are 2 python csv files:

`py_g.csv` and `py_u.csv` : `g` and `u` denote gaussian and uniform noise respectively

## Plots

There are 2 directories, gaussian and uniform.

The images follow a naming convention in accordance with those from the `images` folder. There are 2 types of images:
- `<image><number>_<noise_type>` 
- `_<image>_avg_<noise_type>` (Represent the average hamming distances)

`<image>` : the type of image; m = male face, f = female face, o = object

`<noise_type>` : u (uniform) and g (gaussian)

`<number>` : a number ranging from 1 to 4 (correspond to image names from the images folder)

