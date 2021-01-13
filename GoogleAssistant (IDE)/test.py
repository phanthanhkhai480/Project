from youtubesearchpython import SearchVideos

search = SearchVideos("NoCopyrightSounds", offset = 1, mode = "json", max_results = 20)
print(search.result())