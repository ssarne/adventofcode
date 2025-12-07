def read_lines(file_path: str) -> list[str]:
    """
    Read all lines from a file and return them as a list of strings.
    
    Args:
        file_path: Path to the file to read
        
    Returns:
        List of strings, one for each line in the file
        
    Raises:
        FileNotFoundError: If the file does not exist
    """
    with open(file_path, "r") as f:
        return [line.strip() for line in f.readlines()] 
