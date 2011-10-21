function im = drawbyteyline(im, gray, gree, encoded)
% Getting the color
sizes = size(gray);
segx = sizes(2);
segy = sizes(1);
for i=1:9
	if (encoded(i) == 0)
		im(1:segy, ((i-1)*segx+1):(i*segx),:) = gray;
	else
		im(1:segy, ((i-1)*segx+1):(i*segx),:) = gree;
	end
end
