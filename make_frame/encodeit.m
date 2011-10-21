% Getting the encoded sequence
function encoded = encodeit(original)
encoded = zeros(1, length(original)+1);
prev = 0;
for i=1:length(original)
	if (original(i) == 0)
		encoded(i+1) = encoded(i);
	else
		encoded(i+1) = mod(encoded(i)+1, 2);
	end
	prev = original(i);
end
end
