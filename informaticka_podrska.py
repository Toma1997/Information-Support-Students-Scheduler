
import numpy as np
from scipy.optimize import linprog

# broj sati za svakog studenta da bi bio min trosak
# Ciljna funkcija F = 2100x1 + 2200x2 + 2000x3 + 1900x4 + 2400x5 + 2500x6
# min 8 sati nedeljno za bachelor studente -> x1 + x2 + x3 + x4 >= 32
# min 7 sati nedeljno za master studente -> x5 + x6 >= 14
# min sati za celu nedelju da se pokrije ceo dan x1 + x2 + x3 + x4 + x5 + x6 = 70
# x1 <= 18, x2 <= 12, x3 <= 20, x4 <= 20, x5 <= 14, x6 <= 8


# !(podeljeno sa -1 da bi smo dobili ogranicenje <=)!


# Dodaj koeficijente uz x1, x2 iz svake nejednacine
A_ub = np.array([[-1, -1, -1, -1, 0, 0],
                 [0, 0, 0, 0, -1, -1]])

# Redom cuvaj rezultate (ogranicenja nejednacina)
b_ub = np.array([-32,
                 -14])

A_eq = np.array([[1, 1, 1, 1, 1, 1]])
b_eq = np.array([70])

# Koeficijenti ciljne F funkicje redom x1, x2, x3, x4, x5, x6
c = np.array([2100, 2200, 2000, 1900, 2400, 2500]) # Metod uvek minimizuje F

# Koristi se Simpleks metod za resavanje problema linearnog programiranja iz biblioteke linprog 
res = linprog(c, A_ub=A_ub, b_ub=b_ub, A_eq=A_eq, b_eq=b_eq, bounds=(7, 20), method='simplex')

# Prikazi formatirane vrednosti
print('Minimalni trosak:', int(res.fun))
print("x1= ", int(round(res.x[0])))
print("x2= ", int(round(res.x[1])))
print("x3= ", int(round(res.x[2])))
print("x4= ", int(round(res.x[3])))
print("x5= ", int(round(res.x[4])))
print("x6= ", int(round(res.x[5])))