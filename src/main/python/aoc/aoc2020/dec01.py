import sys

lines = list(map(int, open("../resources/aoc2020/dec01.txt", "r").readlines()))

for i in range(0, len(lines)):
    for j in range(i + 1, len(lines)):
        if int(lines[i]) + int(lines[j]) == 2020:
            print(lines[i], "*", lines[j], "=", int(lines[i]) * int(lines[j]))

for i in range(0, len(lines)):
    for j in range(i + 1, len(lines)):
        for k in range(j + 1, len(lines)):
            if int(lines[i]) + int(lines[j]) + int(lines[k]) == 2020:
                print(lines[i], "*", lines[j], "*", lines[k], "=", int(lines[i]) * int(lines[j]) * int(lines[k]))
