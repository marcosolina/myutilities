# After Hand Brake

I use [HandBrake](https://handbrake.fr/) to compress my videos. Unfortunatelly this tool removes some of the exif info from the files, like the GPS coordinates, so wrote this simple tool to copy the exif info from the original video file, to the one compressed with Hand Brake

## How to use
- Compress the videos with HandBrake
- Keep the Original file in one folder, and the compressed one in a different folder
- Update the properties in the application.properties
- Run the java

# Notes:
- The compressed files procuced with HandBrake should have the same file name as the source, and the file extension must be ".mp4"