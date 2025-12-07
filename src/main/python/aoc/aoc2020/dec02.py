from aoc.utils import read_lines

lines = read_lines("input/aoc2020/dec02.txt")
valid_count = 0
for line in lines:
    # Split line into three parts
    parts = line.split()
        
    # Get min and max from first part
    range_parts = parts[0].split('-')
    min_val = int(range_parts[0])
    max_val = int(range_parts[1])
    
    # Get character from second part (remove colon)
    c = parts[1][0]  # parts[1] is like "a:", so we take first character
    
    # Count occurrences of c in password
    count = parts[2].count(c)
    
    # Check if count is within range
    if min_val <= count <= max_val:
        valid_count += 1
        
print(f"{valid_count}")

valid_count = 0
for line in lines:
    # Split line into three parts
    parts = line.split()
        
    # Get min and max from first part
    range_parts = parts[0].split('-')
    pos1 = int(range_parts[0])
    pos2 = int(range_parts[1])
    
    # Get character from second part (remove colon)
    c = parts[1][0]  # parts[1] is like "a:", so we take first character
    
    # Check if character is at exactly one position (XOR)
    if (parts[2][pos1-1] == c) != (parts[2][pos2-1] == c):
        valid_count += 1
        
print(f"{valid_count}")
