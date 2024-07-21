# deepfake-detection-using-perceptual-hashes

This repository contains code and data for various investigations on detecting deepfakes using perceptual hashes.

<p align="center">
  <img src="https://github.com/user-attachments/assets/67f2d58f-c5db-4578-bc9b-c5dd9e1505a1" alt="sample" width="450">
</p>


There are 2 perceptual hashing programs in consideration:
- Python implementation (https://pypi.org/project/ImageHash/)
- Scala implementation (https://gist.github.com/Howon/7db1239355841a71ffa9)

The investigations on various aspects have been divided into disjoint directories:
- `noise` : Investigations related to noise sensitivity
- `object` : Investigations related to detection in object images
- `face-swap` : Investigations related to detection in face-swap images
- `face-modifications` : Investigations related to detection of generic facial feature modifications
