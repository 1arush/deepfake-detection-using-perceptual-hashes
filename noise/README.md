# README

- The script 'add_noise.py' iterates over images and adds noise (uniform and gaussian) to them.
- The code 'noising.py' is responsible for iterating over the noised and source images, computing hashes using ImageHash
and storing the hamming distances in a csv file for further manipulations
- The code 'noising.scala' does the same as the aforementioned python version, but it makes use of the Scala program. It then
writes hamming distances to a similar csv.

Note : The csv files for gaussian and uniform noise have been separated for simplification.
