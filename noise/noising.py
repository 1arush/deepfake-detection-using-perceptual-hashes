from PIL import Image
import imagehash, os
import numpy as np
import pandas as pd

SIZE=8
names = []

for c in ["f","m","o"]:
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

unif = {}
gaus = {}

for name in names:
    unif[name]=[]
    gaus[name]=[]


for name in names:
    for noise in range(20,241,20):
        basei = f'C:/Users/HP/Downloads/work/{name}/{name}.jpg'
        unifi = f'C:/Users/HP/Downloads/work/{name}/{name}u_{noise}.jpg'
        gausi = f'C:/Users/HP/Downloads/work/{name}/{name}g_{noise}.jpg'

        img = imagehash.phash(Image.open(basei),hash_size=SIZE)
        uni = imagehash.phash(Image.open(unifi),hash_size=SIZE)
        gas = imagehash.phash(Image.open(gausi),hash_size=SIZE)

        udst = img - uni
        gdst = img - gas

        unif[name].append(udst)
        gaus[name].append(gdst)

    print(f'{name} processed!')

print(unif)

udf = pd.DataFrame(unif)
gdf = pd.DataFrame(gaus)

udf.to_csv('C:/users/hp/downloads/py_u.csv',index=False)
gdf.to_csv('C:/users/hp/downloads/py_g.csv',index=False)