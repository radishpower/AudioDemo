frameno = floor(rand(8, 1)*255)
headervalue = 200;
segx = 40;
segy = 40;
w = segx*9;
gray = zeros(segy, segx, 3); gray(:,:,1) = 128/255.0; gray(:,:,2) = 128/255.0; gray(:,:,3) = 128/255.0;
gree = zeros(segy, segx, 3); gree(:,:,1) = 10/255.0; gree(:,:,2) = 128/255.0; gree(:,:,3) = 10/255.0;
im = zeros(segy, w, 3);

original = bitget(headervalue, 8:-1:1);
encoded = encodeit(original)
resultim = drawbyteyline(im, gray, gree, encoded);
for index = 1:length(frameno)
	original = bitget(frameno(index), 8:-1:1);
	encoded = encodeit(original);
	resultim_newline = drawbyteyline(im, gray, gree, encoded);
	resultim = vertcat(resultim, resultim_newline);
end
imshow(resultim);
