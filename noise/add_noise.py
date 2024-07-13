from PIL import Image
import imagehash, os
import numpy as np
import pandas as pd

def add_uniform_noise(image, low=-10, high=10):
    arr = np.array(image)
    noise = np.random.uniform(low, high, arr.shape).astype(np.int16)
    mod_img = arr.astype(np.int16) + noise
    mod_img = np.clip(mod_img,0,255).astype(np.uint8)
    return Image.fromarray(mod_img)

def add_gaussian_noise(image, mean=0, std=10):
    arr = np.array(image)
    noise = np.random.normal(mean, std, arr.shape).astype(np.int16)
    mod_img = arr.astype(np.int16) + noise
    mod_img = np.clip(mod_img, 0, 255).astype(np.uint8)
    return Image.fromarray(mod_img)

names = []
for c in ["m","f","o"]:
    li = []
    if(c=="m" or c=="f"):
        li=[1,2]
    else:
        li=[1,2,3,4]
    for i in li:
        ns=c+str(i)
        names.append(ns)
print(names)
# quit()

for name in names:
    for noise in range(20,241,20):
        base = f'C:/Users/HP/Downloads/work/{name}/{name}.jpg'
        unif = add_uniform_noise(Image.open(base),-noise,noise)
        gaus = add_gaussian_noise(Image.open(base),0,noise)

        un_path = f'C:/Users/HP/Downloads/work/{name}/{name}u_{noise}.jpg'
        ga_path = f'C:/Users/HP/Downloads/work/{name}/{name}g_{noise}.jpg'

        unif.save(un_path)
        gaus.save(ga_path)

        # print(un_path, ga_path)
