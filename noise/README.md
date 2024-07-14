# noise sensitivity

- The script 'add_noise.py' iterates over images and adds noise (uniform and gaussian) to them.
- The code 'noising.py' is responsible for iterating over the noised and source images, computing hashes using ImageHash
and storing the hamming distances in a csv file for further manipulations
- The code 'noising.scala' does the same as the aforementioned python version, but it makes use of the Scala program. It then
writes hamming distances to a similar csv.
- The code 'noise_plots.ipynb' is used to make all the plots for noise sensitivity, including plots for individual images as well as averages.
All the plots have been made using the csv files in the 'csv' directory.

Note : The csv files for gaussian and uniform noise have been separated for simplification.

<p align="center">
  <img src="https://github.com/user-attachments/assets/c858f6d3-4bd4-45e7-bd76-f078bc0b45bd" alt="noise_sample">
</p>
